package com.heddxh.kupo.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.heddxh.kupo.R
import com.heddxh.kupo.util.Quest

@Composable
fun QuestProgress(
    quest: Quest,
    progress: Float,
    modifier: Modifier = Modifier,
    onDrag: (Float) ->
    Unit = {}
) {
    val dragOffset = remember { mutableFloatStateOf(0f) }
    Box(
        contentAlignment = Alignment.Center, modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
            .clip(MaterialTheme.shapes.large)
            .height(IntrinsicSize.Min)
            .paint(
                painter = painterResource(id = R.drawable._200px_logo_5_0),
                alpha = .3f,
                sizeToIntrinsics = false,
                contentScale = ContentScale.Crop
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // Call onDrag method there to avoid calling it multiple times in one drag.
                        onDrag(dragOffset.floatValue)
                        dragOffset.floatValue = 0f
                    }
                ) { _, dragAmount ->
                    dragOffset.floatValue = dragAmount
                }
            }
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
                    text = quest.title, style = MaterialTheme.typography.titleLarge
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
                .animateContentSize(
                    tween( //FIXME: 更明显的动画效果
                        durationMillis = 200,
                        delayMillis = 0,
                        easing = EaseOutBounce
                    )
                )
                .fillMaxWidth(progress)
                .align(Alignment.CenterStart)
        )
    }
}

/*
@Preview
@Composable
fun QuestProgressPreview() {
    KupoTheme {
        QuestProgress(Quest())
    }
}*/
