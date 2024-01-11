package com.heddxh.kupo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heddxh.kupo.data.NewsItem
import com.heddxh.kupo.data.NewsRepository
import com.heddxh.kupo.data.QuestsRepository
import com.heddxh.kupo.network.NetworkService
import com.heddxh.kupo.network.beginnerSearchProvider
import com.heddxh.kupo.network.model.News
import com.heddxh.kupo.network.model.SearchResult
import com.heddxh.kupo.network.wikiSearchProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val networkService: NetworkService,
    private val questsRepository: QuestsRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        val ioScope = CoroutineScope(Dispatchers.IO)
        // 下载任务数据
        ioScope.launch {
            val currentCount = questsRepository.getCurrentCount()
            if (currentCount == 0) {
                Log.d("HomeViewModel", "Quests need to download")
                networkService.downloadQuestsData(questsRepository)
            } else {
                Log.d("HomeViewModel", "init: $currentCount, don't need to download")
            }
            val questsItems = questsRepository.getAllItems()
            val count = questsItems.size
            val quests = mutableListOf<Quest>()
            if (count == 0) {
                Log.e("HomeViewModel", "Can't get quests")
            }
            for (item in questsItems) {
                quests.add(
                    Quest(
                        id = item.id,
                        title = item.name,
                        progress = item.id / count.toFloat(),
                    )
                )
            }
            _uiState.update { currentState ->
                currentState.copy(quests = quests)
            }
        }

        // 下载新闻
        ioScope.launch {
            val newsList = mutableListOf<News>()
            // Fixme: If network is quick, newsList will flash because of recomposition
            Log.d("HomeViewModel", "Getting News...")
            networkService.getNews().apply {
                if (this.isNotEmpty()) {
                    this.forEach {
                        newsList.add(it)
                        newsRepository.insert(
                            NewsItem(
                                link = it.link,
                                homeImagePath = it.homeImagePath,
                                publishDate = it.publishDate,
                                sortIndex = it.sortIndex,
                                summary = it.summary,
                                title = it.title
                            )
                        )
                    }
                } else {
                    Log.e("HomeViewModel", "Can't fetch latest news, using local instead")
                    newsRepository.getAllItems().apply {
                        if (this.isNotEmpty()) {
                            this.forEach {
                                newsList.add(
                                    News(
                                        link = it.link,
                                        homeImagePath = it.homeImagePath,
                                        publishDate = it.publishDate,
                                        sortIndex = it.sortIndex,
                                        summary = it.summary,
                                        title = it.title
                                    )
                                )
                            }
                        } else {
                            Log.e("HomeViewModel", "Local news is empty, using placeholder")
                            repeat(5) {
                                newsList.add(fakeNews)
                            }
                        }
                    }
                }
                _uiState.update { currentState ->
                    currentState.copy(newsList = newsList)
                }
            }
        }
    }

    fun toggleSearchBarStatus() {
        _uiState.update { currentState ->
            currentState.copy(searchViewEnable = !currentState.searchViewEnable, searchQuery = "")
        }
    }

    fun search(query: String = "") {
        viewModelScope.launch {
            val searchResult =
                networkService.search(
                    query,
                    listOf(beginnerSearchProvider, wikiSearchProvider)
                )
            _uiState.update { currentState ->
                currentState.copy(searchResults = searchResult, isSearchLoading = false)
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                searchViewEnable = true,
                searchQuery = query,
                isSearchLoading = true
            )
        }
    }

    fun progressDrag(offset: Float) {
        if (_uiState.value.quests.isEmpty()) {
            Log.e("HomeViewModel", "Quests are empty, disable drag")
            return
        }
        val currentID = _uiState.value.currentQuest.id
        Log.d("HomeViewModel", "current ID: $currentID, offset: $offset")
        _uiState.update {
            if ((offset < 0) and (currentID > 0)) {
                // 向左
                it.copy(currentQuest = it.quests[currentID - 2])// 修正下标与ID的偏移
            } else if ((offset > 0) and (currentID < it.quests.size)) {
                // 向右
                it.copy(currentQuest = it.quests[currentID + 1])
            } else {
                it
            }
        }
    }
}

private val fakeNews = News(
    link = "https://ff.sdo.com/web6/news/newsContent/2021/08/31/5309_929456.html",
    homeImagePath = "https://ff.sdo.com/web6/news/newsContent/2021/08/31/5309_929456.jpg",
    publishDate = "2021-08-31 17:00:00",
    sortIndex = 1,
    summary = "《最终幻想XIV》与《最终幻想XV》联动活动“暗影之逆焰”将于2021年9月13日（周一）开始！",
    title = "《最终幻想XIV》与《最终幻想XV》联动活动“暗影之逆焰”将于2021年9月13日（周一）开始！"
)

/**
 * State data class for HomeViewModel
 * @param newsList list of [News] to display
 * @param searchViewEnable Weather the full-screen search view is enable or not
 * @param searchQuery the query string user searching
 * @param searchResults list of [SearchResult] to display
 * @param isSearchLoading indicate whether the search is loading from the network
 * @param currentQuest current [Quest] displayed in QuestProgress, TODO: Should get from local data
 * @param quests list of [Quest] Stored for QuestProgress
 */
data class HomeUiState(
    // News
    val newsList: List<News> = emptyList(),
    // Search
    val searchViewEnable: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val isSearchLoading: Boolean = false,
    // Quest
    val currentQuest: Quest = Quest(),
    val quests: List<Quest> = emptyList()
)

data class Quest(
    val id: Int = 66,
    val version: String = "5.0",
    val title: String = "地面冷若冰霜，天空遥不可及",
    val progress: Float = (66 / 106.toFloat()),
    val versionTitle: String = "暗影之逆焰主线任务",
)