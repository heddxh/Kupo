package com.heddxh.kupo.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.heddxh.kupo.R
import com.heddxh.kupo.network.model.News
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsCarousel(cards: List<News>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = {
        cards.size
    })
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 16.dp,
        pageSize = PageSize.Fixed(320.dp),
    ) { page ->
        NewsCard(data = cards[page], modifier = modifier
            .graphicsLayer {
                // Calculate the absolute offset for the current page from the
                // scroll position. We use the absolute value which allows us to mirror
                // any effects for both directions
                val pageOffset = (
                        (pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue
                // We animate the alpha, between 50% and 100%
                alpha = lerp(
                    start = .5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsCard(data: News, modifier: Modifier = Modifier) {
    Card(
        onClick = {},
        modifier = modifier
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
                model = data.homeImagePath,
                contentDescription = data.title,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
        val textModifier = Modifier.padding(horizontal = 16.dp)
        Text(
            text = data.title,
            modifier = textModifier,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = data.summary,
            modifier = textModifier,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = false, device = "id:pixel_4_xl")
@Composable
fun NewsCarouselPreview() {
    NewsCarousel(fakeCardsList)
}

private val fakeCardsList = listOf(
    News(
        title = "9/9-9/10 《最终幻想14》参展北京核聚变2023！",
        summary = "最终幻想14将于9月9日-9月10日参展北京核聚变2023！",
        link = "https://ff.web.sdo.com/web8/index.html#/newstab/newscont/352734",
        homeImagePath = "https://fu5.web.sdo.com/10036/202308/16922658666728.jpg",
        publishDate = "2023/08/18 10:59:40",
        sortIndex = 10
    ),
    News(
        title = "9/9-9/10 《最终幻想14》参展北京核聚变2023！",
        summary = "最终幻想14将于9月9日-9月10日参展北京核聚变2023！",
        link = "https://ff.web.sdo.com/web8/index.html#/newstab/newscont/352734",
        homeImagePath = "https://fu5.web.sdo.com/10036/202308/16922658666728.jpg",
        publishDate = "2023/08/18 10:59:40",
        sortIndex = 10
    ),
    News(
        title = "9/9-9/10 《最终幻想14》参展北京核聚变2023！",
        summary = "最终幻想14将于9月9日-9月10日参展北京核聚变2023！",
        link = "https://ff.web.sdo.com/web8/index.html#/newstab/newscont/352734",
        homeImagePath = "https://fu5.web.sdo.com/10036/202308/16922658666728.jpg",
        publishDate = "2023/08/18 10:59:40",
        sortIndex = 10
    ),
)
