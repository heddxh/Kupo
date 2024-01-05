package com.heddxh.kupo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heddxh.kupo.data.FakeNewsRepository
import com.heddxh.kupo.data.FakeQuestRepository
import com.heddxh.kupo.network.FakeNetworkService
import com.heddxh.kupo.ui.components.NewsCarousel
import com.heddxh.kupo.ui.components.QuestProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, homeViewModel: HomeViewModel = viewModel()
) {
    val homeUiState by homeViewModel.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
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
                modifier = Modifier.widthIn(
                    min = LocalConfiguration.current.screenWidthDp.dp - 32.dp
                )
            ) {
                LazyColumn {
                    items(homeUiState.searchResult) {
                        Text(it.title)
                    }
                }

            }
            QuestProgress(
                quest = homeUiState.quest,
                onDrag = homeViewModel::progressDrag,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            NewsCarousel(
                cards = homeUiState.newsList
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        homeViewModel = HomeViewModel(
            FakeNetworkService(), FakeQuestRepository(), FakeNewsRepository()
        )
    )
}


