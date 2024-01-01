package com.heddxh.kupo.network.model

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
)
