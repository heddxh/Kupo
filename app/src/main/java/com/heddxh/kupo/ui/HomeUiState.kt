package com.heddxh.kupo.ui

import com.heddxh.kupo.network.model.News
import com.heddxh.kupo.network.model.newBeeSearchSingle

data class HomeUiState(
    //News
    val newsList: List<News> = emptyList(),
    //Search
    val searchViewEnable: Boolean = false,
    val searchQuery: String = "库啵！",
    val searchResult: List<newBeeSearchSingle> = emptyList(),
    //Quest
    val quest: Quest = Quest(),
    val quests: List<Quest> = emptyList()
)

data class Quest(
    val id: Int = 66,
    val version: String = "5.0",
    val title: String = "地面冷若冰霜，天空遥不可及",
    val progress: Float = (66 / 106.toFloat()),
    val versionTitle: String = "暗影之逆焰主线任务",
)