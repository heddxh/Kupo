package com.heddxh.kupo.network

import com.heddxh.kupo.data.QuestsRepository
import com.heddxh.kupo.network.model.News
import com.heddxh.kupo.network.model.SearchResult

interface NetworkService {

    suspend fun getNews(): List<News>

    suspend fun downloadQuestsData(questsRepository: QuestsRepository)

    suspend fun search(query: String, providers: List<SearchProvider>): List<SearchResult>
}