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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heddxh.kupo.data.FakeNewsRepository
import com.heddxh.kupo.data.FakeQuestRepository
import com.heddxh.kupo.network.FakeNetworkService
import com.heddxh.kupo.ui.components.NewsCarousel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
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
        NewsCarousel(
            cards = homeUiState.newsList,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp)
        )
        /*HomeList(
            newsList = homeUiState.newsList,
            quest = homeUiState.quest,
            onDrag = { homeViewModel.progressDrag(it) },
            contentPadding = PaddingValues(
                top = (WindowInsets //FIXME: 非硬编码方式显示在搜索栏下方
                    .statusBars
                    .getTop(LocalDensity.current)
                    .dp) + 32.dp
            ),
            modifier = modifier.padding(horizontal = 16.dp)
        )*/
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        homeViewModel = HomeViewModel(
            FakeNetworkService(),
            FakeQuestRepository(),
            FakeNewsRepository()
        )
    )
}


