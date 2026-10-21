package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

val SINGLE_SHAPE = RoundedCornerShape(16.dp)
val TOP_SHAPE =
    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
val MIDDLE_SHAPE = RoundedCornerShape(4.dp)
val BOTTOM_SHAPE =
    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)

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
        .padding(16.dp)
}

@Composable
fun Modifier.bubble(index: Int, listSize: Int): Modifier {
    return this
        .bubbleShape(index, listSize)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(16.dp)
}

@Composable
fun InfoCard(
    @DrawableRes iconId: Int? = null,
    title: String,
    description: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .conditionalModifier(onClick != null) {
                clickable { onClick?.invoke() }
            }
            .conditionalModifier(iconId != null) {
                padding(16.dp)
            }
            .conditionalModifier(iconId == null) {
                padding(top = 16.dp, bottom = 16.dp, start = 32.dp, end = 32.dp)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconId?.let {
            Box(
                Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(iconId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.width(16.dp))
        }

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


@Composable
fun ListDivider(
    color: Color = MaterialTheme.colorScheme.background
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        HorizontalDivider(thickness = 2.dp, color = color)
    }
}

@Composable
fun VerticalListDivider(
    color: Color = MaterialTheme.colorScheme.background
) {
    Box(
        Modifier
            .fillMaxHeight()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        VerticalDivider(thickness = 2.dp, color = color)
    }
}