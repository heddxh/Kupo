package com.heddxh.kupo.remote.search

import com.heddxh.kupo.remote.dto.newBeeSearchSingle
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.compression.ContentEncoding

interface SearchService {
    suspend fun getSearch(): List<newBeeSearchSingle>

    companion object {
        fun create(): SearchService {
            return SearchServiceImpl(
                client = HttpClient(Android) {
                    install(ContentEncoding) {
                        gzip()
                    }
                }
            )
        }
    }
}