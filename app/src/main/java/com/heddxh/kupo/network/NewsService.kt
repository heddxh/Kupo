package com.heddxh.kupo.network

import com.heddxh.kupo.network.dto.News
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

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