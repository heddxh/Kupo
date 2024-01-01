package com.heddxh.kupo.network

import com.heddxh.kupo.data.QuestsRepository
import com.heddxh.kupo.network.model.News
import com.heddxh.kupo.network.model.newBeeSearchSingle

interface NetworkService {

    suspend fun getNews(): List<News>

    suspend fun search(query: String): List<newBeeSearchSingle>

    suspend fun downloadQuestsData(questsRepository: QuestsRepository)
}