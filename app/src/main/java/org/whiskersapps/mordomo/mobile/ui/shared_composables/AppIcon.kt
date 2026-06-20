package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.features.apps.App

@Composable
fun AppIcon(
    themed: Boolean,
    size: Dp? = null,
    icons: App.Icons,
    shape: Shape = CircleShape
) {

    val isAdaptive by remember {
        derivedStateOf {
            if (themed) {
                if (icons.themed?.isAdaptive == true) {
                    icons.themed.adaptive?.background != null && icons.themed.adaptive?.foreground != null
                } else {
                    false
                }
            } else {
                if (icons.stock.isAdaptive) {
                    icons.stock.adaptive?.background != null && icons.stock.adaptive?.foreground != null
                } else {
                    false
                }
            }
        }
    }


    if (isAdaptive) {
        val icon by remember {
            derivedStateOf {
                if (themed) {
                    icons.themed!!.adaptive!!
                } else {
                    icons.stock.adaptive!!
                }
            }
        }

        val backgroundState = produceState<ImageBitmap?>(initialValue = null, icons, themed) {
            value = withContext(Dispatchers.Default) {
                val height =
                    if (icon.foreground.intrinsicHeight > 0) icon.foreground.intrinsicHeight else 128

                val width =
                    if (icon.foreground.intrinsicWidth > 0) icon.foreground.intrinsicWidth else 128

                icon.background!!.toBitmap(width = width, height = height).asImageBitmap()
            }
        }

        val foregroundState = produceState<ImageBitmap?>(initialValue = null, icons, themed) {
            value = withContext(Dispatchers.Default) {
                val height =
                    if (icon.foreground.intrinsicHeight > 0) icon.foreground.intrinsicHeight else 128

                val width =
                    if (icon.foreground.intrinsicWidth > 0) icon.foreground.intrinsicWidth else 128

                icon.foreground.toBitmap(width = width, height = height).asImageBitmap()
            }
        }

        Box(
            modifier = Modifier
                .conditionalModifier(size != null) {
                    this.size(size!!)
                }
                .conditionalModifier(size == null) {
                    this.fillMaxWidth()
                }
                .clip(shape),
            contentAlignment = Alignment.Center
        ) {

            backgroundState.value?.let { background ->
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.48f),
                    bitmap = background,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
            foregroundState.value?.let { foreground ->
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.48f),
                    bitmap = foreground,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    } else {
        val icon by remember {
            derivedStateOf {
                if (themed) {
                    icons.themed!!.drawable.toBitmap().asImageBitmap()
                } else {
                    icons.stock.drawable.toBitmap().asImageBitmap()
                }
            }
        }


        Box(
            modifier = Modifier
                .conditionalModifier(size != null) {
                    this.size(size!!)
                }
                .conditionalModifier(size == null) {
                    this.fillMaxWidth()
                }
                .clip(shape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .conditionalModifier(size != null) {
                        this.size(size!!)
                    }
                    .conditionalModifier(size == null) {
                        this.fillMaxWidth()
                    }, bitmap = icon, contentDescription = null
            )
        }
    }
}