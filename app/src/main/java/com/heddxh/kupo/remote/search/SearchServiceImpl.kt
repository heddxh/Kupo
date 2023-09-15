package com.heddxh.kupo.remote.search

import com.heddxh.kupo.remote.dto.newBeeSearch
import com.heddxh.kupo.remote.dto.newBeeSearchSingle
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json

class SearchServiceImpl(
    private val client: HttpClient
) : SearchService {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getSearch(query: String): List<newBeeSearchSingle> {
        val BASE_URL = "https://novice-network-search.wakingsands.com/s"
        val response = client.get(BASE_URL) {
            headers {
                append(HttpHeaders.AcceptEncoding, "gzip")
            }
            url {
                parameters.append("word", query)
                parameters.append("pn", "1")
                parameters.append("ps", "10")
            }
        }
        val body: String = response.body()
        return try {
            Json.decodeFromString<newBeeSearch>(body).results
        }
        catch (e: MissingFieldException){
            println(body)
            emptyList()
        }
    }
}