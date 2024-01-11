package com.heddxh.kupo.ui.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heddxh.kupo.data.FakeNewsRepository
import com.heddxh.kupo.data.FakeQuestRepository
import com.heddxh.kupo.network.FakeNetworkService

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
                placeholder = { Text("库啵！") },
                query = if (homeUiState.searchViewEnable)
                    homeUiState.searchQuery else "",
                onQueryChange = {
                    if (it.isNotEmpty()) {
                        homeViewModel.search(it)
                    }
                },
                onSearch = { },
                active = homeUiState.searchViewEnable,
                onActiveChange = {
                    homeViewModel.toggleSearchBarStatus()
                },
                modifier = Modifier.widthIn(
                    min = LocalConfiguration.current.screenWidthDp.dp - 32.dp
                )
            ) {
                val state = rememberLazyListState()
                if (state.isScrollInProgress) {
                    Log.d("SearchView", "Be Evaluated")
                    LocalSoftwareKeyboardController.current?.hide()
                }
                LazyColumn(
                    state = state,
                ) {
                    if (homeUiState.isSearchLoading) {
                        item {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                    }
                    items(homeUiState.searchResults) {
                        val urlHandler = LocalUriHandler.current
                        ListItem(
                            modifier = Modifier.clickable {
                                urlHandler.openUri(it.url)
                            },
                            overlineContent = { Text(it.provider.toString()) },
                            headlineContent = { Text(it.title) },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Info, contentDescription = null,
                                    modifier = Modifier.size(40.dp)
                                )
                            },
                        )
                    }
                }

            }
            //FIXME: Do not put below composable in a column
            // or when search view enable, they will be squeezed down
            QuestProgress(
                quest = homeUiState.currentQuest,
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


