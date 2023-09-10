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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.heddxh.kupo.ui.theme.KupoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KupoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home(imageList = imageData)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(imageList: List<Int>, modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    var extraTopPd by remember { mutableStateOf(8.dp) }
    Scaffold(
        modifier = modifier,
        topBar = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = extraTopPd),
                contentAlignment = Alignment.Center
            ) {
                SearchBar(
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
                    /*TODO*/
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(top = 32.dp)
        ) {
            Carousel(imageList = imageList)
            Spacer(Modifier.height(8.dp))
            Cards()
        }
    }
}

@Composable
fun Cards(modifier: Modifier = Modifier) {
    LazyColumn(contentPadding = PaddingValues(16.dp), modifier = modifier) {
        items(imageData) { item ->
            CardItem(image = item, Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun CardItem(@DrawableRes image: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Image(
            painter = painterResource(image),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(@DrawableRes imageList: List<Int>, modifier: Modifier = Modifier) {
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

@Preview
@Composable
fun HomePreview() {
    KupoTheme {
        Home(imageList = imageData)
    }
}

@Preview
@Composable
fun CardsPreview() {
    KupoTheme {
        Cards()
    }
}

private val imageData = listOf(
    R.drawable.screenshot_20230905_163858, R.drawable.screenshot_20230905_163916,
    R.drawable.screenshot_20230905_164822, R.drawable.screenshot_20230905_165437,
    R.drawable.screenshot_20230905_173444, R.drawable.screenshot_20230905_173454,
    R.drawable.screenshot_20230905_174304, R.drawable.screenshot_20230905_214857,
    R.drawable.screenshot_20230905_222346, R.drawable.screenshot_20230905_222813
)
