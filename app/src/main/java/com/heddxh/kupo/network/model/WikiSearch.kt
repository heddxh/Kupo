package com.heddxh.kupo.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WikiSearch(
    val query: String,
    val results: List<String>,
    val summary: List<String>
)