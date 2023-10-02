package com.heddxh.kupo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heddxh.kupo.network.NetworkService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val networkService = NetworkService.create()

    suspend fun updateNews() {
        _uiState.update { currentState ->
            currentState.copy(newsList = networkService.getNews())
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
        //TODO: 动画以及方向修正，不要让进度条超出范围
        Log.d("HomeViewModel", "progressDrag: $offset")
        _uiState.update {
            if (offset > 100) {
                it.copy(quest = it.quest.copy(progress = it.quest.progress - .05f))
            } else {
                it.copy(quest = it.quest.copy(progress = it.quest.progress + .05f))
            }
        }
    }
}