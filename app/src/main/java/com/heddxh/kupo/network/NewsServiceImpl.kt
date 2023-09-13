package com.heddxh.kupo.network

import com.heddxh.kupo.network.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

class NewsServiceImpl(
    private val client: HttpClient
    ) : NewsService {

        override suspend fun getNews(): List<News> {
            val BASE_URL = "https://cqnews.web.sdo.com/api/news/newsList"
            return try {
                val response = client.get(BASE_URL) {
                    headers {
                        append(HttpHeaders.Accept, "text/html")
                        append(HttpHeaders.AcceptEncoding, "gzip")
                    }
                    url {
                        parameters.append("gameCode", "ff")
                        parameters.append("CategoryCode", "5309,5310,5311,5312,5313")
                        parameters.append("pageIndex", "0")
                        parameters.append("pageSize", "5")
                        parameters.append("callback", "_jsonp14nht6cminm")
                    }
                }
                val rawBody: String = response.body()
                val dirtyIndexStart = rawBody.indexOf('(')
                val dirtyIndexEnd = rawBody.lastIndexOf(')')
                val rawBodyData = Json.decodeFromString<RawBodyClass>(rawBody.substring(dirtyIndexStart + 1, dirtyIndexEnd))
                rawDataToNews(rawBodyData.Data)
            } catch(e: RedirectResponseException) {
                // 3xx - responses
                println("Error: ${e.response.status.description}")
                emptyList()
            } catch(e: ClientRequestException) {
                // 4xx - responses
                println("Error: ${e.response.status.description}")
                emptyList()
            } catch(e: ServerResponseException) {
                // 5xx - responses
                println("Error: ${e.response.status.description}")
                emptyList()
            } catch(e: Exception) {
                println("Error: ${e.message}")
                emptyList()
            }
        }
}


private fun rawDataToNews(rawData: List<NewsData>): List<News> {
    val newslist: MutableList<News> = mutableListOf()
    for (item in rawData) {
        newslist.add(News(
            link = item.Author,
            homeImagePath = item.HomeImagePath,
            publishDate = item.PublishDate,
            sortIndex = item.SortIndex,
            summary = item.Summary,
            title = item.Title
        ))
    }
    return newslist
}