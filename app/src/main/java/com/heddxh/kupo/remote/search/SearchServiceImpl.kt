package com.heddxh.kupo.remote.search

import com.heddxh.kupo.remote.dto.newBeeSearch
import com.heddxh.kupo.remote.dto.newBeeSearchSingle
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

class SearchServiceImpl(
    private val client: HttpClient
) : SearchService {

    override suspend fun getSearch(): List<newBeeSearchSingle> {
        val BASE_URL = "https://novice-network-search.wakingsands.com/s"
        val response = client.get(BASE_URL) {
            headers {
                append(HttpHeaders.AcceptEncoding, "gzip")
            }
            url {
                parameters.append("word", "1")
                parameters.append("pn", "1")
                parameters.append("ps", "10")
            }
        }
        val bodyStr: String = response.body()
        return Json.decodeFromString<newBeeSearch>(bodyStr).results
    }
}