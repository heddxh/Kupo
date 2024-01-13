package com.heddxh.kupo.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.heddxh.kupo.util.Quest

@Entity(tableName = "quests", indices = [Index(value = ["version"])])
data class QuestItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val version: String,
    val name: String
) {
    fun toQuest() = Quest(
        id = id,
        version = version,
        title = name
    )
}
