package com.heddxh.kupo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.heddxh.kupo.remote.NewsService
import com.heddxh.kupo.remote.dto.News
import com.heddxh.kupo.remote.dto.newBeeSearchSingle
import com.heddxh.kupo.remote.search.SearchService
import com.heddxh.kupo.ui.theme.KupoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val newsService = NewsService.create()
    private val searchService = SearchService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val newsList by produceState<List<News>>(
                initialValue = emptyList(),
                producer = {
                    value = newsService.getNews()
                }
            )
            var query by remember { mutableStateOf("") }
            /* val searchList by produceState<List<newBeeSearchSingle>>(
                initialValue = emptyList(),
                query
            ) {
                    value = searchService.getSearch("")
            } */
            var searchList: List<newBeeSearchSingle> by remember { mutableStateOf(emptyList()) }
            KupoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scope = rememberCoroutineScope()
                    Home(newsList = newsList, searchList = searchList, query = query) {
                        query = it
                        scope.launch {
                            searchList = searchService.getSearch(query)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    newsList: List<News> = fakeNewsList,
    searchList: List<newBeeSearchSingle> = fakeSearchList,
    query: String,
    onQueryChange: (String) -> Unit
) {
    // var query by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    Column(modifier = Modifier) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            SearchBar( // TODO: 宽度修正
                modifier = Modifier.align(Alignment.TopCenter),
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {},
                active = searchActive,
                onActiveChange = { state ->
                    searchActive = state
                },
                placeholder = { Text("库啵！") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            ) {
                // TODO: 搜索实现
                for (item in searchList) {
                    ListItem(
                        headlineContent = { Text(item.title) },
                        supportingContent = { Text(item.body) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier
            .height(16.dp)
            .padding(horizontal = 16.dp))
        Cards(newsList = newsList, modifier.padding(horizontal = 16.dp))
    }
}

@Composable
private fun Cards(newsList: List<News>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(newsList) { news ->
            CardItem(
                imagePath = news.homeImagePath,
                title = news.title,
                summary = news.summary,
            )
        }
    }
}

@Composable
private fun CardItem(
    imagePath: String,
    title: String,
    summary: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        AsyncImage(
            model = imagePath,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = summary,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    searchList: List<newBeeSearchSingle> = fakeSearchList
) {
    SearchBar(
        query = "query",
        onQueryChange = { },
        onSearch = {},
        active = true,
        onActiveChange = { },
        placeholder = { Text("库啵！") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
    ) {
        for (item in searchList) {
            ListItem(
                headlineContent = { Text(item.title) },
                supportingContent = { Text(item.body) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Carousel(@DrawableRes imageList: List<Int>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        imageList.size
    }
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 64.dp),
        pageSpacing = 16.dp,
        modifier = modifier
    ) { index ->
        Image(
            painter = painterResource(id = imageList[index]),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        )
    }
}

@Preview(device = "id:pixel_4_xl", showBackground = true)
@Composable
fun HomePreview() {
    KupoTheme {
        //Home()
    }
}

private val fakeSingleNews = News(
    link = "https://ff.web.sdo.com/web8/index.html#/newstab/newscont/352734",
    homeImagePath =  "https://fu5.web.sdo.com/10036/202308/16922658666728.jpg",
    publishDate =  "2023/08/18 10:59:40",
    sortIndex = 10,
    summary = "最终幻想14将于9月9日-9月10日参展北京核聚变2023！",
    title = "9/9-9/10 《最终幻想14》参展北京核聚变2023！"
)

private val fakeNewsList = listOf(
    fakeSingleNews,
    fakeSingleNews,
    fakeSingleNews,
    fakeSingleNews,
    fakeSingleNews,
    fakeSingleNews
)

@Preview
@Composable
private fun CardItemPreview() {
    Card(modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.test),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Title",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "最终幻想14将于9月9日-9月10日参展北京核聚变2023！",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview
@Composable
fun SearchViewPreview() {
    KupoTheme {
        SearchView()
    }
}

private val fakeSearchSingle = newBeeSearchSingle(
    url = "duty/16.htm",
    title = "最终决战天幕魔导城",
    body = " 最终决战天幕魔导城 2.0 主线最后一个副本，冒险者变成大英雄的开端，副本主旨是，一路狂奔。事后西德会把副本里乘坐的魔导机甲送给玩家 (哦副本本身就是跑路 MAX……)。中途尽量不要死，不吃技能就不",
)

private val fakeSearchList = listOf(
    fakeSearchSingle,
    fakeSearchSingle,
    fakeSearchSingle,
    fakeSearchSingle,
    fakeSearchSingle,
    fakeSearchSingle,
    fakeSearchSingle,
    fakeSearchSingle,
    fakeSearchSingle,
    fakeSearchSingle,
)


