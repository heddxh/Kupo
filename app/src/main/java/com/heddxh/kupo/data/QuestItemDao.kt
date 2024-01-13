package com.heddxh.kupo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questItem: QuestItem)

    @Query("SELECT * FROM quests WHERE id = :id")
    suspend fun getQuestFromId(id: Int): QuestItem

    @Query("SELECT * FROM quests WHERE version = :version")
    suspend fun getQuestsFromVersion(version: String): List<QuestItem>

    @Query("SELECT * FROM quests ORDER BY id ASC")
    suspend fun getAllQuests(): List<QuestItem>

    @Query("SELECT COUNT(*) FROM quests WHERE version = :version")
    suspend fun countFromVersion(version: String): Int

    @Query("SELECT id FROM quests WHERE version = :version ORDER BY id ASC LIMIT 1")
    suspend fun getVersionFstId(version: String): Int

    @Query("SELECT id FROM quests WHERE version = :version ORDER BY id DESC LIMIT 1")
    suspend fun getVersionLstId(version: String): Int
}