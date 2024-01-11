package com.heddxh.kupo.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class News(
    val link: String,
    val homeImagePath: String,
    val publishDate: String,
    val sortIndex: Int,
    val summary: String,
    val title: String,
)

@Serializable
@Suppress("SpellCheckingInspection")
data class NewsData(
    @SerialName("Id") val id: Int,
    @SerialName("ApplicationCode") val applicationCode: Int,
    @SerialName("Articletype") val articleType: Int,
    @SerialName("Author") val author: String,
    @SerialName("CategoryCode") val categoryCode: Int,
    @SerialName("GroupIndex") val groupIndex: Int,
    @SerialName("HomeImagePath") val homeImagePath: String,
    @SerialName("OutLink") val outLink: String,
    @SerialName("PublishDate") val publishDate: String,
    @SerialName("SortIndex") val sortIndex: Int,
    @SerialName("Summary") val summary: String,
    @SerialName("Title") val title: String,
    @SerialName("TitleClass") val titleClass: String
) {
    fun toNews() = News(
        link = author,
        homeImagePath = homeImagePath,
        publishDate = publishDate,
        sortIndex = sortIndex,
        summary = summary,
        title = title
    )
}

@Serializable
data class RawBodyClass(
    @SerialName("Data") val data: List<NewsData>,
    @SerialName("Code") val code: String,
    @SerialName("Message") val message: String,
    @SerialName("PageCount") val pageCount: Int,
    @SerialName("TotalCount") val totalCount: Int,
    @SerialName("RecordCount") val recordCount: Int
)