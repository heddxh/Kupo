package com.heddxh.kupo.data

import android.content.Context

interface AppContainer {
    val itemsRepository: QuestsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [QuestsRepository]
     */
    override val itemsRepository: QuestsRepository by lazy {
        OfflineQuestsItemsRepository(QuestsDB.getDatabase(context).itemDao())
    }
}