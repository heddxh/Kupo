package com.heddxh.kupo.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.heddxh.kupo.R
import com.heddxh.kupo.ui.Quest
import com.heddxh.kupo.ui.theme.KupoTheme

@Composable
fun QuestProgress(quest: Quest, modifier: Modifier = Modifier, onDrag: (Float) -> Unit = {}) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
            .clip(MaterialTheme.shapes.large)
            .draggable(
                state = rememberDraggableState(onDelta = onDrag),
                orientation = Orientation.Horizontal
            )
            .height(IntrinsicSize.Min)
            .paint(
                painter = painterResource(id = R.drawable._200px_logo_5_0),
                alpha = .3f,
                sizeToIntrinsics = false,
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = quest.title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = quest.version,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = quest.versionTitle,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = .5F),
                    shape = MaterialTheme.shapes.large
                )
                .fillMaxHeight()
                .animateContentSize(spring(.5f))
                .fillMaxWidth(quest.progress)
                .align(Alignment.CenterEnd)
        )
    }
}

@Preview
@Composable
fun QuestProgressPreview() {
    KupoTheme {
        QuestProgress(Quest())
    }
}