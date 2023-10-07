package com.heddxh.kupo.data

import kotlinx.coroutines.flow.Flow

interface QuestsRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<QuestItem>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Int): Flow<QuestItem?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(questItem: QuestItem)

}