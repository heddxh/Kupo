package com.heddxh.kupo.remote

import com.heddxh.kupo.remote.dto.News
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.compression.ContentEncoding

interface NewsService {
    suspend fun getNews(): List<News>

    companion object {
        fun create(): NewsService {
            return NewsServiceImpl(
                client = HttpClient(Android) {
                    install(ContentEncoding) {
                        gzip()
                    }
                }
            )
        }
    }
}