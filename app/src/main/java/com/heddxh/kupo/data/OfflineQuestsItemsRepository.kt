package com.heddxh.kupo.data

import kotlinx.coroutines.flow.Flow

class OfflineQuestsItemsRepository(private val itemDao: QuestItemDao) : QuestsRepository {
    override fun getAllItemsStream(): Flow<List<QuestItem>> = itemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<QuestItem?> = itemDao.getItem(id)

    override suspend fun insertItem(questItem: QuestItem) = itemDao.insert(questItem)

}