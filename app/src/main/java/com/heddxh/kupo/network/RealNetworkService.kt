@file:Suppress("SpellCheckingInspection")

package com.heddxh.kupo.network

import android.util.Log
import com.heddxh.kupo.data.QuestItem
import com.heddxh.kupo.data.QuestsRepository
import com.heddxh.kupo.network.model.News
import com.heddxh.kupo.network.model.RawBodyClass
import com.heddxh.kupo.network.model.SearchResult
import com.heddxh.kupo.util.QuestsUtil
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.resources.Resource
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup

private const val NEWS_LIST_URL = "cqnews.web.sdo.com"
private const val WIKI_URL = "ff14.huijiwiki.com"

class RealNetworkService : NetworkService {
    private val client = NetworkClient.client

    override suspend fun getNews(): List<News> {
        try {
            val response = client.get(NewsRes()) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = NEWS_LIST_URL
                }
                headers {
                    append(HttpHeaders.Accept, "text/html")
                }
                url {
                    parameters.append("gameCode", "ff")
                    parameters.append("CategoryCode", "5309,5310,5311,5312,5313")
                    parameters.append("pageIndex", "0")
                    parameters.append("pageSize", "5")
                    parameters.append("callback", "data")
                }
            }
            return cleanNewsData(response.body())
        } catch (e: Exception) {
            // Handle network error here.
            Log.e("getNews", e.message ?: "")
            return emptyList()
        }
    }

    override suspend fun downloadQuestsData(questsRepository: QuestsRepository, version: String) {
        if (!QuestsUtil.isValidVersion(version)) {
            // Log and return in advance since the version is invalid.
            Log.e("NetworkService", "Invalid Version String: $version")
            return
        }
        val response = try {
            client.get(WikiRes.WikiQuestsRes(version = QuestsUtil(version).name)) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = WIKI_URL
                }
            }
        } catch (e: Exception) {
            Log.e("downloadQuestsData", e.message ?: "")
            return
        }
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
            questsRepository.insertQuest(
                QuestItem(
                    name = quest,
                    version = version
                )
            )
        }
    }

    override suspend fun search(
        query: String,
        providers: List<SearchProvider>
    ): List<SearchResult> {
        // TODO: 各个 SearchProvider 返回结果权重
        val results = mutableListOf<SearchResult>()
        providers.forEach { results += it(query) }
        return results
    }
}

/**
 * Clean newslist response.
 * @param responseString jsonp which start which looks like:
 * ```Jsonp
 * data({ MY JSON DATA })
 * ```
 */
private fun cleanNewsData(responseString: String): List<News> {
    val dirtyIndexStart = responseString.indexOf('(')
    val dirtyIndexEnd = responseString.lastIndexOf(')')
    val rawBodyData = Json.decodeFromString<RawBodyClass>(
        responseString.substring(dirtyIndexStart + 1, dirtyIndexEnd)
    )
    return rawBodyData.data.map { it.toNews() }
}

@Resource("/api/news/newsList")
class NewsRes

@Resource("/wiki")
class WikiRes {
    @Resource("{version}")
    class WikiQuestsRes(val parent: WikiRes = WikiRes(), val version: String)
}
