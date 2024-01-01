package com.heddxh.kupo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heddxh.kupo.data.NewsItem
import com.heddxh.kupo.data.NewsRepository
import com.heddxh.kupo.data.QuestsRepository
import com.heddxh.kupo.network.NetworkService
import com.heddxh.kupo.network.model.News
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

    //private val questsRepository = QuestsRepository(DataDB.getDatabase(context).questItemDao())
    //private val newsRepository = NewsRepository(DataDB.getDatabase(context).newsDao())

    init {
        val ioScope = CoroutineScope(Dispatchers.IO)
        ioScope.launch {
            val currentCount = questsRepository.getCurrentCount()
            if (currentCount == 0) {
                Log.d("HomeViewModel", "Need to download")
                networkService.downloadQuestsData(questsRepository)
            } else {
                Log.d("HomeViewModel", "init: $currentCount, don't need to download")
            }
            val questsItems = questsRepository.getAllItems()
            val count = questsItems.size
            val quests = mutableListOf<Quest>()
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
        ioScope.launch {
            val newsItems = newsRepository.getAllItems()
            val newsList = mutableListOf<News>()
            if (newsItems.isEmpty()) { //FIXME: 判断新闻更新时机
                Log.d("HomeViewModel", "Need to download")
                networkService.getNews().forEach {
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
                newsItems.forEach {
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
                Log.d("HomeViewModel", "init: ${newsItems.size}, don't need to download")
            }
            _uiState.update { currentState ->
                currentState.copy(newsList = newsList)
            }
        }
    }

    fun search(query: String = "", active: Boolean) {
        if (active) {
            viewModelScope.launch {
                val searchResult = networkService.search(query)
                _uiState.update { currentState ->
                    currentState.copy(searchResult = searchResult)
                }
            }
            _uiState.update { currentState ->
                currentState.copy(
                    searchViewEnable = true,
                    searchQuery = query,
                )
            }
        } else {
            _uiState.update {
                it.copy(searchViewEnable = false, searchQuery = "库啵！")
            }
        }
    }

    fun progressDrag(offset: Float) {
        //FIXME: 更好的滑动控制
        val currentID = _uiState.value.quest.id
        Log.d("HomeViewModel", "current ID: $currentID, offset: $offset")
        _uiState.update {
            if ((offset < -200) and (currentID > 0)) {
                // 向左
                it.copy(quest = it.quests[currentID - 2])// 修正下标与ID的偏移
            } else if ((offset > 200) and (currentID < it.quests.size)) {
                // 向右
                it.copy(quest = it.quests[currentID + 1])
            } else {
                it
            }
        }
    }
}