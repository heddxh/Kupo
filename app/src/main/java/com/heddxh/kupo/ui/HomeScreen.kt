package com.heddxh.kupo.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.heddxh.kupo.data.OfflineQuestsItemsRepository
import com.heddxh.kupo.data.QuestsDB
import com.heddxh.kupo.network.dto.News
import com.heddxh.kupo.ui.components.HomeList
import com.heddxh.kupo.ui.theme.KupoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    db: QuestsDB = QuestsDB.getDatabase(LocalContext.current),
    homeViewModel: HomeViewModel = HomeViewModel(OfflineQuestsItemsRepository(db.itemDao())),
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
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
            LazyColumn {
                items(homeUiState.searchResult) {
                    Text(it.title)
                }
            }

        }
        HomeList(
            newsList = homeUiState.newsList,
            quest = homeUiState.quest,
            onDrag = { homeViewModel.progressDrag(it) },
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}


@Preview
@Composable
fun NewsListPreview() {
    KupoTheme {
        HomeList(quest = Quest(), newsList = fakeNewsList, onDrag = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenFake(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState = HomeUiState(newsList = fakeNewsList, searchViewEnable = false),
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
        }
        HomeList(
            newsList = homeUiState.newsList,
            quest = Quest(),
            onDrag = {},
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



