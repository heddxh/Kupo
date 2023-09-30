package com.heddxh.kupo.network

import com.heddxh.kupo.network.DTO.News
import com.heddxh.kupo.network.DTO.NewsData
import com.heddxh.kupo.network.DTO.RawBodyClass
import com.heddxh.kupo.network.DTO.newBeeSearch
import com.heddxh.kupo.network.DTO.newBeeSearchSingle
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

private const val NEWS_LIST_URL = "https://cqnews.web.sdo.com/api/news/newsList"
private const val SEARCH_BEGINNER_URL = "https://novice-network-search.wakingsands.com/s"


private fun cleanNewsData(responseString: String): List<News> {
    val dirtyIndexStart = responseString.indexOf('(')
    val dirtyIndexEnd = responseString.lastIndexOf(')')
    val rawBodyData = Json.decodeFromString<RawBodyClass>(
            responseString.substring(dirtyIndexStart + 1, dirtyIndexEnd)
    )
    return rawDataToNews(rawBodyData.Data)
}

private fun rawDataToNews(rawData: List<NewsData>): List<News> {
    val newsList: MutableList<News> = mutableListOf()
    for (item in rawData) { //TODO: MAP?
        newsList.add(
                News(
                        link = item.Author,
                        homeImagePath = item.HomeImagePath,
                        publishDate = item.PublishDate,
                        sortIndex = item.SortIndex,
                        summary = item.Summary,
                        title = item.Title
                )
        )
    }
    return newsList
}

class NetworkServiceImpl(private val client: HttpClient) : NetworkService {

    override suspend fun getNews(): List<News> {
        val response = client.get(NEWS_LIST_URL) {
            headers {
                append(HttpHeaders.Accept, "text/html")
            }
            url {
                parameters.append("gameCode", "ff")
                parameters.append("CategoryCode", "5309,5310,5311,5312,5313")
                parameters.append("pageIndex", "0")
                parameters.append("pageSize", "5")
                parameters.append("callback", "_jsonp14nht6cminm")
            }
        }
        return cleanNewsData(response.body())
    }

    override suspend fun search(query: String): List<newBeeSearchSingle> {
        val response = client.get(SEARCH_BEGINNER_URL) {
            headers {
                append(HttpHeaders.AcceptEncoding, "gzip")
            }
            url {
                parameters.append("word", query)
                parameters.append("pn", "1")
                parameters.append("ps", "10")
            }
        }
        return response.body<newBeeSearch>().results
    }
}