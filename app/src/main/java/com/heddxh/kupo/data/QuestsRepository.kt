package com.heddxh.kupo.data

import android.util.Log
import com.heddxh.kupo.util.QuestsUtil

interface QuestsRepository {

    suspend fun getAllQuests(): List<QuestItem>

    suspend fun getQuestsByVersion(version: String): List<QuestItem>

    suspend fun insertQuest(questItem: QuestItem)

    suspend fun checkCompletionPerVersion(version: String): Boolean

    suspend fun countByVersion(version: String): Int

    suspend fun getCurrVerRange(version: String): Pair<Int, Int>
}

class RealQuestsRepository(private val itemDao: QuestItemDao) : QuestsRepository {

    override suspend fun getAllQuests(): List<QuestItem> = itemDao.getAllQuests()

    override suspend fun getQuestsByVersion(version: String): List<QuestItem> {
        if (QuestsUtil.isValidVersion(version)) {
            return itemDao.getQuestsFromVersion(version)
        } else {
            Log.e("QuestsRepository", "Invalid Version String: $version")
            return emptyList()
        }
    }

    override suspend fun insertQuest(questItem: QuestItem) = itemDao.insert(questItem)

    override suspend fun checkCompletionPerVersion(version: String): Boolean {
        if (QuestsUtil.isValidVersion(version)) {
            val count = countByVersion(version)
            Log.d("QuestsRepository", "Count from repo: $count")
            Log.d("QuestsRepository", "Count should be: ${QuestsUtil(version).number}")
            return count == QuestsUtil(version).number
        } else {
            Log.e("QuestsRepository", "Invalid Version String: $version")
            return true
        }
    }

    override suspend fun countByVersion(version: String): Int {
        if (QuestsUtil.isValidVersion(version)) {
            return itemDao.countFromVersion(version)
        } else {
            Log.e("QuestsRepository", "Invalid Version String: $version")
            return 0
        }
    }

    override suspend fun getCurrVerRange(version: String): Pair<Int, Int> {
        return Pair(itemDao.getVersionFstId(version), itemDao.getVersionLstId(version))
    }
}
