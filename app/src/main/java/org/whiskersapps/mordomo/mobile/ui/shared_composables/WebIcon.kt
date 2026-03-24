package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import org.whiskersapps.mordomo.mobile.R

@Composable
fun WebIcon(
    url: String,
    size: Dp = 32.dp,
    showRealIcon: Boolean = true
) {
    val context = LocalContext.current
    val tintColor = MaterialTheme.colorScheme.onBackground

    val tintedPlaceholder = remember(tintColor) {
        ContextCompat.getDrawable(context, R.drawable.globe)?.apply {
            setTint(tintColor.toArgb())
        }
    }

    if (showRealIcon) {
        val request = remember(url) {
            ImageRequest.Builder(context)
                .data(url)
                .placeholder(tintedPlaceholder)
                .error(tintedPlaceholder)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
        }

        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(size),
            model = request,
            contentDescription = null
        )
    } else {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .size(size),
            painter = painterResource(R.drawable.globe),
            contentDescription = null,
            tint = tintColor
        )
    }
}