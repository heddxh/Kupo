package com.heddxh.kupo.network

import com.heddxh.kupo.network.DTO.News
import com.heddxh.kupo.network.DTO.newBeeSearchSingle
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface NetworkService {

    suspend fun getNews(): List<News>

    suspend fun search(query: String): List<newBeeSearchSingle>

    companion object {
        fun create(): NetworkService {
            return NetworkServiceImpl(
                    client = HttpClient(Android) {
                        install(ContentEncoding) {
                            gzip()
                        }
                        install(ContentNegotiation) {
                            json(
                                    Json {
                                        prettyPrint = true
                                        isLenient = true
                                        ignoreUnknownKeys = true
                                    }
                            )
                        }
                        defaultRequest {
                            header("AcceptEncoding", "gzip")
                        }
                    }
            )
        }
    }
}