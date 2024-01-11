package com.heddxh.kupo.network.model

import kotlinx.serialization.Serializable

private const val FF14ORG_URL = "https://ff14.org/"

@Serializable
data class BeginnerSearch(
    val total: Int,
    val pageSize: Int,
    val pageNumber: Int,
    val results: List<SingleBeginnerSearch>
) {
    fun getSearchResults(): List<SearchResult> {
        return results.map {
            SearchResult(
                url = FF14ORG_URL + it.url,
                title = it.title,
                summary = it.body,
                provider = SearchProviderName.Beginner
            )
        }
    }
}

@Serializable
data class SingleBeginnerSearch(
    val url: String,
    val title: String,
    val body: String,
)
