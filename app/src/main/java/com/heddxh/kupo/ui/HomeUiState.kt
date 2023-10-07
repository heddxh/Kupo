package com.heddxh.kupo.ui

import com.heddxh.kupo.network.dto.News
import com.heddxh.kupo.network.dto.newBeeSearchSingle

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

data class Quest(
    val version: String = "5.0",
    val title: String = "地面冷若冰霜，天空遥不可及",
    val progress: Float = .5f,
    val versionTitle: String = "暗影之逆焰主线任务",
)