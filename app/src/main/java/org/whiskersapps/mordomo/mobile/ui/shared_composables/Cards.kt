package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class CardShape {
    Top,
    Middle,
    Bottom,
    Single,
    None
}

@Composable
fun Modifier.cardShape(shape: CardShape): Modifier {
    return when (shape) {
        CardShape.Top -> this.clip(
            RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = 4.dp,
                bottomEnd = 4.dp
            )
        )

        CardShape.Middle -> this.clip(
            RoundedCornerShape(4.dp)
        )

        CardShape.Bottom -> this.clip(
            RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 4.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        )

        CardShape.Single -> this.clip(RoundedCornerShape(16.dp))

        CardShape.None -> this.clip(RectangleShape)
    }
}

@Composable
fun Modifier.card(
    shape: CardShape,
    padding: Dp = 8.dp,
    onClick: (() -> Unit)? = null
): Modifier {
    return this
        .fillMaxWidth()
        .height(intrinsicSize = IntrinsicSize.Min)
        .cardShape(shape)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .conditionalModifier(onClick != null) { this.clickable { onClick?.invoke() } }
        .padding(padding)
}


fun getCardShape(index: Int, listSize: Int): CardShape {
    return if (index == 0 && listSize == 1) {
        CardShape.Single
    } else if (index == 0) {
        CardShape.Top
    } else if (index + 1 == listSize) {
        CardShape.Bottom
    } else {
        CardShape.Middle
    }
}