package com.heddxh.kupo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quests")
data class QuestItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
