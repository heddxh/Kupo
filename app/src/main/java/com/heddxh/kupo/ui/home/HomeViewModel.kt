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
import com.heddxh.kupo.util.Quest
import com.heddxh.kupo.util.QuestsUtil
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
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow() // Export read-only state for Ui

    init {
        val ioScope = CoroutineScope(Dispatchers.IO)

        // Check weather all quests are downloaded. If not, download them.
        ioScope.launch {
            _uiState.update { it.copy(isQuestLoading = true) }
            val validVersions = QuestsUtil.validVersions()
            validVersions.forEach {
                if (questsRepository.checkCompletionPerVersion(it)) {
                    Log.d(
                        "HomeViewModel",
                        "Quests of ${QuestsUtil(it).name} don't need to download"
                    )
                } else {
                    Log.d(
                        "HomeViewModel",
                        "Quests of ${QuestsUtil(it).name} need to download"
                    )
                    networkService.downloadQuestsData(questsRepository, it) //TODO: 并发
                }
            }
            val currentVersion = _uiState.value.currentQuest.version
            val (fstId, lstId) = questsRepository.getCurrVerRange(currentVersion)
            if (currentVersion in validVersions) {
                val currentVersionQuests = questsRepository.getQuestsByVersion(currentVersion)
                _uiState.update {
                    it.copy(
                        quests = currentVersionQuests.map { questItem ->
                            questItem.toQuest()
                        },
                        isQuestLoading = false,
                        currentQuest = Quest(), // TODO: get from user local data
                        currRange = Pair(fstId, lstId)
                    )
                }
                Log.d("HomeViewModel", "Prepared for quests")
            } else {
                Log.e("HomeViewModel", "Current version is invalid: $currentVersion")
            }
        }

        // Fetch News
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
        val currentId = _uiState.value.currentQuest.id
        Log.d("HomeViewModel", "current ID: $currentId, offset: $offset")
        _uiState.update {
            if ((offset < 0) and (currentId >= it.currRange.first)) {
                // Left, previous
                it.copy(
                    currentQuest = it.quests[it.quests.indexOf(it.currentQuest) - 1],
                    progress = it.progress - (1f / QuestsUtil(it.currentQuest.version).number)
                )
            } else if ((offset > 0) and (currentId <= it.currRange.second)) {
                // Right, next
                it.copy(
                    currentQuest = it.quests[it.quests.indexOf(it.currentQuest) + 1],
                    progress = it.progress + (1f / QuestsUtil(it.currentQuest.version).number)
                )
            } else {
                it
            }
        }
    }
}


/**
 * State data class for HomeViewModel
 * @param newsList list of [News] to display
 * @param searchViewEnable Weather the full-screen search view is enable or not
 * @param searchQuery the query string user searching
 * @param searchResults list of [SearchResult] to display
 * @param isSearchLoading indicate whether the search is loading from the network
 * @param currentQuest current [Quest] displayed in QuestProgress
 * @param currRange id range of current version
 * @param progress for QuestProgress to display current progress
 * @param quests list of [Quest] Stored for QuestProgress
 * @param isQuestLoading indicate whether the quest is loading from the network
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
    val currRange: Pair<Int, Int> = Pair(217, 322),
    val progress: Float = (66 / 106.toFloat()),
    val quests: List<Quest> = emptyList(),
    val isQuestLoading: Boolean = false
)

private val fakeNews = News(
    link = "https://ff.sdo.com/web6/news/newsContent/2021/08/31/5309_929456.html",
    homeImagePath = "https://ff.sdo.com/web6/news/newsContent/2021/08/31/5309_929456.jpg",
    publishDate = "2021-08-31 17:00:00",
    sortIndex = 1,
    summary = "《最终幻想XIV》与《最终幻想XV》联动活动“暗影之逆焰”将于2021年9月13日（周一）开始！",
    title = "《最终幻想XIV》与《最终幻想XV》联动活动“暗影之逆焰”将于2021年9月13日（周一）开始！"
)