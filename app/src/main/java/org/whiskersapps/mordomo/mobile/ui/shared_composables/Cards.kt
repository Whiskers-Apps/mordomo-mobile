package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

val SINGLE_SHAPE = RoundedCornerShape(16.dp)
val TOP_SHAPE =
    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
val MIDDLE_SHAPE = RoundedCornerShape(4.dp)
val BOTTOM_SHAPE =
    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)

@Composable
fun Modifier.bubblePadding(): Modifier {
    return this.padding(
        top = 12.dp,
        bottom = 12.dp,
        start = 16.dp,
        end = 16.dp,
    )
}

@Composable
fun Modifier.bubbleShape(index: Int, listSize: Int): Modifier {
    if (listSize == 1)
        return this.clip(SINGLE_SHAPE)

    if (index == 0)
        return this.clip(TOP_SHAPE)

    if (index == listSize - 1)
        return this.clip(BOTTOM_SHAPE)

    return this.clip(MIDDLE_SHAPE)
}

@Composable
fun Modifier.buttonBubble(index: Int, listSize: Int, onClick: () -> Unit): Modifier {
    return this
        .bubbleShape(index, listSize)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .clickable { onClick() }
        .bubblePadding()
}

@Composable
fun Modifier.bubble(index: Int, listSize: Int): Modifier {
    return this
        .bubbleShape(index, listSize)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .bubblePadding()
}

@Composable
fun InfoCard(
    shape: Shape,
    title: String,
    description: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .conditionalModifier(onClick != null) {
                clickable { onClick?.invoke() }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}