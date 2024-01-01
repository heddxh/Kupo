package com.heddxh.kupo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsItem(
    val link: String,
    val homeImagePath: String,
    val publishDate: String,
    @PrimaryKey val sortIndex: Int,
    val summary: String,
    val title: String
)
