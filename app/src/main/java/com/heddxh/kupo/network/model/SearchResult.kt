package com.heddxh.kupo.network.model

import kotlinx.serialization.Serializable

enum class SearchProviderName {
    Beginner {
        override fun toString(): String = "ff14.org"
    },

    Wiki {
        override fun toString(): String = "Wiki"
    };
}

@Serializable
data class SearchResult(
    val url: String,
    val title: String,
    val summary: String,
    val provider: SearchProviderName
)
