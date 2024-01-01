package com.heddxh.kupo.data

interface NewsRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    suspend fun getAllItems(): List<NewsItem>

    /**
     * Insert item in the data source
     */
    suspend fun insert(newsItem: NewsItem)
}

class RealNewsRepository(private val newsDao: NewsDao) : NewsRepository {

    override suspend fun insert(newsItem: NewsItem) = newsDao.insert(newsItem)
    override suspend fun getAllItems(): List<NewsItem> = newsDao.getAllItems()

}

class FakeNewsRepository : NewsRepository {

    private val fakeSingleNews = NewsItem(
        link = "https://ff.web.sdo.com/web8/index.html#/newstab/newscont/352734",
        homeImagePath = "https://fu5.web.sdo.com/10036/202308/16922658666728.jpg",
        publishDate = "2023/08/18 10:59:40",
        sortIndex = 10,
        summary = "最终幻想14将于9月9日-9月10日参展北京核聚变2023！",
        title = "9/9-9/10 《最终幻想14》参展北京核聚变2023！"
    )

    override suspend fun getAllItems(): List<NewsItem> = listOf(
        fakeSingleNews,
        fakeSingleNews,
        fakeSingleNews,
        fakeSingleNews,
        fakeSingleNews,
        fakeSingleNews,
        fakeSingleNews,
    )

    override suspend fun insert(newsItem: NewsItem) {
        TODO("Not yet implemented")
    }
}