package com.heddxh.kupo.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class newBeeSearch(
    val total: Int,
    val pageSize: Int,
    val pageNumber: Int,
    val results: List<newBeeSearchSingle>
)

@Serializable
data class newBeeSearchSingle(
    val url: String,
    val title: String,
    val body: String,
    val highlights: newBeeSearchSingleHighlight = newBeeSearchSingleHighlight(listOf("123"), listOf("123"))
)

@Serializable
data class newBeeSearchSingleHighlight(
    val title: List<String> = listOf("123"),
    val body: List<String> = listOf("123")
)
