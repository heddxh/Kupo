package com.heddxh.kupo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuestItem::class, NewsItem::class], version = 1)
abstract class DataDB : RoomDatabase() {

    abstract fun questItemDao(): QuestItemDao
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var Instance: DataDB? = null

        /**
         * Singleton: if the Instance is not null, return it, otherwise create a new database
         * instance.
         */
        fun getDatabase(context: Context): DataDB {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DataDB::class.java, "kupo_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}