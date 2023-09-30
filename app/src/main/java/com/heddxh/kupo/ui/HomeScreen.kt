package com.heddxh.kupo.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.heddxh.kupo.R
import com.heddxh.kupo.network.DTO.News
import com.heddxh.kupo.ui.theme.KupoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
        modifier: Modifier = Modifier,
        homeViewModel: HomeViewModel = viewModel(),
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    LaunchedEffect(homeUiState) {
        homeViewModel.updateNews()
    }
    BoxWithConstraints(
            modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
                query = homeUiState.searchQuery,
                onQueryChange = { homeViewModel.search(it, true) },
                onSearch = { },
                active = homeUiState.searchViewEnable,
                onActiveChange = {
                    if (!it) {
                        homeViewModel.search(active = false)
                    } else {
                        homeViewModel.search("", true)
                    }
                },
                modifier = Modifier
                        .align(Alignment.TopCenter)
                        .widthIn(min = maxWidth - 32.dp)
        ) {
            LazyColumn() {
                items(homeUiState.searchResult) {
                    Text(it.title)
                }
            }

        }
        NewsList(
                newsList = homeUiState.newsList,
                modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsList(newsList: List<News>, modifier: Modifier = Modifier) {
    LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(top = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(newsList) {
            val context = LocalContext.current
            Card(
                    onClick = {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                        context.startActivity(webIntent)
                    }
            ) {
                if (LocalInspectionMode.current) {
                    Image(
                            painterResource(id = R.drawable.test),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                    )
                } else {
                    AsyncImage(
                            model = it.homeImagePath,
                            contentDescription = it.title,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                    )
                }
                val textModifier = Modifier.padding(horizontal = 16.dp)
                Text(
                        text = it.title,
                        modifier = textModifier,
                        style = MaterialTheme.typography.headlineSmall
                )
                Text(
                        text = it.summary,
                        modifier = textModifier,
                        style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun NewsListPreview() {
    KupoTheme {
        NewsList(newsList = fakeNewsList)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenFake(
        modifier: Modifier = Modifier,
        homeUiState: HomeUiState = HomeUiState(false, fakeNewsList),
) {
    BoxWithConstraints(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
    ) {
        SearchBar(
                query = homeUiState.searchQuery,
                onQueryChange = { },
                onSearch = { },
                active = homeUiState.searchViewEnable,
                onActiveChange = {},
                modifier = Modifier
                        .align(Alignment.TopCenter)
                        .widthIn(min = maxWidth - 32.dp)
        ) {
            //TODO: SEARCH
        }
        NewsList(
                newsList = homeUiState.newsList,
                modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(device = "id:pixel_4_xl", showBackground = true)
@Composable
fun HomeScreenPreview() {
    KupoTheme {
        HomeScreenFake()
    }
}

private val fakeSingleNews = News(
        link = "https://ff.web.sdo.com/web8/index.html#/newstab/newscont/352734",
        homeImagePath = "https://fu5.web.sdo.com/10036/202308/16922658666728.jpg",
        publishDate = "2023/08/18 10:59:40",
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



