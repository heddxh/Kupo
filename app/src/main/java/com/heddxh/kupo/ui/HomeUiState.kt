package com.heddxh.kupo.ui

import com.heddxh.kupo.network.DTO.News
import com.heddxh.kupo.network.DTO.newBeeSearchSingle

data class HomeUiState(
        val searchViewEnable: Boolean = false,
        val newsList: List<News> = emptyList(),
        val searchQuery: String = "库啵！",
        val searchResult: List<newBeeSearchSingle> = emptyList()
)
