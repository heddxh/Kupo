package com.heddxh.kupo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuestItem::class], version = 1, exportSchema = false)
abstract class QuestsDB : RoomDatabase() {
    abstract fun itemDao(): QuestItemDao

    companion object {
        @Volatile
        private var Instance: QuestsDB? = null

        fun getDatabase(context: Context): QuestsDB {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, QuestsDB::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }

    }
}