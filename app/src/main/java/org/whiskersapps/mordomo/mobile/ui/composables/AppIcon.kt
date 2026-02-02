package org.whiskersapps.mordomo.mobile.ui.composables

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.features.apps.App

@Composable
fun AppIcon(
    themed: Boolean,
    size: Dp? = null,
    icons: App.Icons
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
                icon.background!!.toBitmap().asImageBitmap()
            }
        }

        val foregroundState = produceState<ImageBitmap?>(initialValue = null, icons, themed) {
            value = withContext(Dispatchers.Default) {
                icon.foreground.toBitmap().asImageBitmap()
            }
        }

        Box(
            modifier = Modifier
                .modifyIf(size != null) {
                    this.size(size!!)
                }
                .modifyIf(size == null) {
                    this.fillMaxWidth()
                }
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {

            backgroundState.value?.let { background ->
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = background,
                    contentDescription = null
                )
            }
            foregroundState.value?.let { foreground ->
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.5f),
                    bitmap = foreground,
                    contentDescription = null
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
                .modifyIf(size != null) {
                    this.size(size!!)
                }
                .modifyIf(size == null) {
                    this.fillMaxWidth()
                }
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .modifyIf(size != null) {
                        this.size(size!!)
                    }
                    .modifyIf(size == null) {
                        this.fillMaxWidth()
                    }, bitmap = icon, contentDescription = null
            )
        }
    }
}