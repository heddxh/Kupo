package com.heddxh.kupo.data

interface QuestsRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItems(): List<QuestItem>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItem(id: Int): QuestItem?

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(questItem: QuestItem)

    suspend fun getCurrentCount(): Int
}

class RealQuestsRepository(private val itemDao: QuestItemDao) : QuestsRepository {

    override fun getAllItems(): List<QuestItem> = itemDao.getAllItems()

    override fun getItem(id: Int) = itemDao.getItem(id)

    override suspend fun insertItem(questItem: QuestItem) = itemDao.insert(questItem)

    override suspend fun getCurrentCount(): Int = itemDao.getCurrentCount()

}

class FakeQuestRepository : QuestsRepository {

    private val quests = mutableListOf(
        QuestItem(
            id = 1,
            name = "Quest 1",
        ),
        QuestItem(
            id = 2,
            name = "Quest 2",
        ),
        QuestItem(
            id = 3,
            name = "Quest 3",
        ),
        QuestItem(
            id = 4,
            name = "Quest 4",
        ),
        QuestItem(
            id = 5,
            name = "Quest 5",
        ),
        QuestItem(
            id = 6,
            name = "Quest 6",
        ),
        QuestItem(
            id = 7,
            name = "Quest 7",
        ),
        QuestItem(
            id = 8,
            name = "Quest 8",
        ),
        QuestItem(
            id = 9,
            name = "Quest 9",
        ),
        QuestItem(
            id = 10,
            name = "Quest 10",
        ),
    )

    override fun getAllItems(): List<QuestItem> = quests

    override fun getItem(id: Int): QuestItem? = quests.find { it.id == id }

    override suspend fun insertItem(questItem: QuestItem) {
        quests.add(questItem)
    }

    override suspend fun getCurrentCount(): Int = quests.size
}
