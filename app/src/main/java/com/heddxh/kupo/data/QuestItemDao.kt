package com.heddxh.kupo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(questItem: QuestItem)

    @Query("SELECT * from quests WHERE id = :id")
    fun getItem(id: Int): QuestItem

    @Query("SELECT * from quests ORDER BY id ASC")
    fun getAllItems(): List<QuestItem>

    //获取表长度
    @Query("SELECT COUNT(*) FROM quests")
    suspend fun getCurrentCount(): Int

}