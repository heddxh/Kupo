package com.heddxh.kupo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.heddxh.kupo.network.NewsService
import com.heddxh.kupo.network.dto.News
import com.heddxh.kupo.ui.theme.KupoTheme

class MainActivity : ComponentActivity() {

    private val service = NewsService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val newsList = produceState<List<News>>(
                initialValue = emptyList(),
                producer = {
                    value = service.getNews()
                }
            )
            KupoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home(newsList.value)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(newsList: List<News> = fakeNewsList, modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    var extraTopPd by remember { mutableStateOf(8.dp) } /*TODO: 正确的全屏动画*/
    Scaffold(
        modifier = modifier,
        topBar = {
            Box(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = extraTopPd),
                contentAlignment = Alignment.Center
            ) {
                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {},
                    active = searchActive,
                    onActiveChange = { state ->
                        searchActive = state
                        extraTopPd = when (state) {
                            true -> 0.dp
                            false -> 8.dp
                        }
                    },
                    placeholder = { Text("库啵！") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                ) {
                    /*TODO: 搜索实现*/
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
        ) {
            /*TODO: 把 Carousel 作为垂直滚动的子集*/
            Spacer(Modifier.height(8.dp))
            Cards(newsList = newsList)
        }
    }
}

@Composable
private fun Cards(newsList: List<News>, modifier: Modifier = Modifier) {
    LazyColumn(contentPadding = PaddingValues(16.dp), modifier = modifier) {
        items(newsList) { news ->
            CardItem(
                image = fetchImage(news.homeImagePath),
                title = news.title,
                summary = news.summary,
                Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun CardItem(
    image: Painter,
    title: String,
    summary: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Image(
            painter = image,
            contentDescription = null
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
        Home()
    }
}

private val imageData = listOf(
    R.drawable.screenshot_20230905_163858, R.drawable.screenshot_20230905_163916,
    R.drawable.screenshot_20230905_164822, R.drawable.screenshot_20230905_165437,
    R.drawable.screenshot_20230905_173444, R.drawable.screenshot_20230905_173454,
    R.drawable.screenshot_20230905_174304, R.drawable.screenshot_20230905_214857,
    R.drawable.screenshot_20230905_222346, R.drawable.screenshot_20230905_222813
)

private val fakeSingleNews = News(
    link = "https://ff.web.sdo.com/web8/index.html#/newstab/newscont/352734",
    homeImagePath =  "https://fu5.web.sdo.com/10036/202308/16922658666728.jpg",
    publishDate =  "2023/08/18 10:59:40",
    sortIndex = 10,
    summary = "最终幻想14将于9月9日-9月10日参展北京核聚变2023！",
    title = "9/9-9/10 《最终幻想14》参展北京核聚变2023！"
)

@Composable
private fun fetchImage(path: String): Painter {
    /*TODO: 加载网络图片*/
    return painterResource(R.drawable.screenshot_20230905_173454)
}

private val fakeNewsList = listOf<News>(
    fakeSingleNews,
    fakeSingleNews,
    fakeSingleNews,
    fakeSingleNews,
    fakeSingleNews,
    fakeSingleNews
)
