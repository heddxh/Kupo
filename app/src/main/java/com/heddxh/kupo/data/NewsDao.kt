package com.heddxh.kupo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(newsItem: NewsItem)

    @Query("SELECT * from news ORDER BY sortIndex ASC")
    suspend fun getAllItems(): List<NewsItem>

}