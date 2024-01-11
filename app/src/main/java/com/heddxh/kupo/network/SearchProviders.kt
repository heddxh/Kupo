package com.heddxh.kupo.network

import android.util.Log
import com.heddxh.kupo.network.model.BeginnerSearch
import com.heddxh.kupo.network.model.SearchProviderName
import com.heddxh.kupo.network.model.SearchResult
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.resources.Resource
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Suppress("SpellCheckingInspection")
private const val SEARCH_BEGINNER_URL = "novice-network-search.wakingsands.com"

@Suppress("SpellCheckingInspection")
private const val WIKI_SEARCH_URL = "ff14.huijiwiki.com"

/**
 * Every search provider should implement this interface, which takes a [String] as query
 * @return a list of [SearchResult]
 */
typealias SearchProvider = suspend (String) -> List<SearchResult>

val beginnerSearchProvider: SearchProvider = beginnerSearchProvider@{ query ->
    if (query == "") {
        return@beginnerSearchProvider emptyList()
    }
    try {
        val response = NetworkClient.client.get(BeginnerSearchRes()) {
            url {
                protocol = URLProtocol.HTTPS
                host = SEARCH_BEGINNER_URL
            }
            headers {
                append(HttpHeaders.AcceptEncoding, "gzip")
            }
            url {
                parameters.append("word", query)
                parameters.append("pn", "1")
                parameters.append("ps", "10")
            }
        }
        response.body<BeginnerSearch>().getSearchResults()
    } catch (e: Exception) {
        Log.e("BeginnerSearch", e.message ?: "")
        emptyList()
    }
}

@Suppress("SpellCheckingInspection")
val wikiSearchProvider: SearchProvider = wikiSearchProvider@{ query ->
    if (query == "") {
        return@wikiSearchProvider emptyList()
    }
    try {
        val response = NetworkClient.client.get(WikiSearchRes()) {
            url {
                protocol = URLProtocol.HTTPS
                host = WIKI_SEARCH_URL
            }
            headers {
                append(HttpHeaders.AcceptEncoding, "gzip")
            }
            url {
                parameters.append("action", "opensearch")
                parameters.append("format", "json")
                parameters.append("formatversion", "2")
                parameters.append("search", query)
            }
        }
        // Index 1 is links, 0 is summary to display, 2 is links
        val raw = cleanWikiResponse(response.body<JsonArray>())
        val (titles, summaries, links) = raw
        titles.mapIndexed { index, title ->
            SearchResult(
                title = title,
                summary = summaries[index],
                url = links[index],
                provider = SearchProviderName.Wiki
            )
        }
    } catch (e: Exception) {
        Log.e("BeginnerSearch", e.message ?: "")
        emptyList()
    }
}

// FIXME: 看懂序列化 JsonArray 的方式
private fun cleanWikiResponse(raw: JsonArray): List<List<String>> {
    return raw.drop(1).map {
        it.jsonArray.map { inner ->
            inner.jsonPrimitive.content
        }
    }
}

@Resource("/s") // Type safe requests
class BeginnerSearchRes

@Resource("/api.php")
class WikiSearchRes