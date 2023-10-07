package com.heddxh.kupo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(questItem: QuestItem)

    @Query("SELECT * from quests WHERE id = :id")
    fun getItem(id: Int): Flow<QuestItem>

    @Query("SELECT * from quests ORDER BY name ASC")
    fun getAllItems(): Flow<List<QuestItem>>

}