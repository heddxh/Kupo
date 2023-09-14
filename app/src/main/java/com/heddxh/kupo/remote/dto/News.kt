package com.heddxh.kupo.remote.dto

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
data class NewsData(
    val Id: Int,
    val ApplicationCode: Int,
    val Articletype: Int,
    val Author: String,
    val CategoryCode: Int,
    val GroupIndex: Int,
    val HomeImagePath: String,
    val OutLink: String,
    val PublishDate: String,
    val SortIndex: Int,
    val Summary: String,
    val Title: String,
    val TitleClass: String
)

@Serializable
data class RawBodyClass(
    val Data: List<NewsData>,
    val Code: String,
    val Message: String,
    val PageCount: Int,
    val TotalCount: Int,
    val RecordCount: Int
)