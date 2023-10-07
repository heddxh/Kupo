package com.heddxh.kupo.network

import com.heddxh.kupo.data.QuestItem
import com.heddxh.kupo.data.QuestsRepository
import com.heddxh.kupo.network.dto.News
import com.heddxh.kupo.network.dto.NewsData
import com.heddxh.kupo.network.dto.RawBodyClass
import com.heddxh.kupo.network.dto.newBeeSearch
import com.heddxh.kupo.network.dto.newBeeSearchSingle
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup

private const val NEWS_LIST_URL = "https://cqnews.web.sdo.com/api/news/newsList"
private const val SEARCH_BEGINNER_URL = "https://novice-network-search.wakingsands.com/s"
private const val WIKI_URL = "https://ff14.huijiwiki.com/wiki"

private val versionMap = mapOf(
    "5.0" to "暗影之逆焰主线任务",
    "6.0" to "晓月之终途主线任务"
)


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

    override suspend fun downloadQuestsDatabase(questsRepository: QuestsRepository) {
        val response = client.get("$WIKI_URL/${versionMap["5.0"]}")
        val doc = Jsoup.parse(response.body<String>())
        val rows = doc
            .select(".wikitable > tbody:nth-child(1)")
            .first()
            ?.getElementsByTag("tr")
            ?.drop(1)
        for (r in rows!!) {
            val quest = r.getElementsByTag("td")[1].getElementsByTag("a")[0]
                .attr("title")
                .drop(3)
            questsRepository.insertItem(QuestItem(name = quest))
        }
    }
}


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