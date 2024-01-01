package com.heddxh.kupo.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.heddxh.kupo.R
import com.heddxh.kupo.network.model.News
import com.heddxh.kupo.ui.Quest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeList(
    newsList: List<News>,
    quest: Quest,
    onDrag: (Float) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            QuestProgress(quest = quest, modifier = Modifier.fillMaxWidth(), onDrag = onDrag)
        }
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