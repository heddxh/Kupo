package com.heddxh.kupo.network

import com.heddxh.kupo.data.QuestsRepository
import com.heddxh.kupo.network.model.News
import com.heddxh.kupo.network.model.SearchResult

class FakeNetworkService : NetworkService {
    override suspend fun getNews(): List<News> {
        return listOf(
            fakeNews,
            fakeNews,
            fakeNews,
            fakeNews,
            fakeNews,
            fakeNews
        )
    }

    override suspend fun downloadQuestsData(questsRepository: QuestsRepository) {
        TODO("Not yet implemented")
    }

    override suspend fun search(
        query: String,
        providers: List<SearchProvider>
    ): List<SearchResult> {
        TODO("Not yet implemented")
    }
}

private val fakeNews = News(
    link = "https://ff.sdo.com/web6/news/newsContent/2021/08/31/5309_929456.html",
    homeImagePath = "https://ff.sdo.com/web6/news/newsContent/2021/08/31/5309_929456.jpg",
    publishDate = "2021-08-31 17:00:00",
    sortIndex = 1,
    summary = "《最终幻想XIV》与《最终幻想XV》联动活动“暗影之逆焰”将于2021年9月13日（周一）开始！",
    title = "《最终幻想XIV》与《最终幻想XV》联动活动“暗影之逆焰”将于2021年9月13日（周一）开始！"
)