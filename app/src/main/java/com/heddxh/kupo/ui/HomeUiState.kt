package com.heddxh.kupo.ui

import com.heddxh.kupo.network.DTO.News
import com.heddxh.kupo.network.DTO.newBeeSearchSingle
import com.heddxh.kupo.ui.components.Quest

data class HomeUiState(
        //News
        val newsList: List<News> = emptyList(),
        //Search
        val searchViewEnable: Boolean = false,
        val searchQuery: String = "库啵！",
        val searchResult: List<newBeeSearchSingle> = emptyList(),
        //Quest
        val quest: Quest = Quest(),
)
