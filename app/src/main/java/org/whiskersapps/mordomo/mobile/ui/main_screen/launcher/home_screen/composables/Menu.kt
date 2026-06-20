package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.shared_composables.BottomCardShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TopCardShape

@Composable
fun MenuDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onSetWallpaperClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    if (show) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(24.dp),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(TopCardShape)
                        .background(MaterialTheme.colorScheme.background)
                        .clickable { onSetWallpaperClick() }
                        .padding(16.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.mountain_outline),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(text = "Set Wallpaper", color = MaterialTheme.colorScheme.onBackground)
                }

                Spacer(
                    Modifier
                        .height(2.dp)
                        .background(Color.Transparent)
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(BottomCardShape)
                        .background(MaterialTheme.colorScheme.background)
                        .clickable { onSettingsClick() }
                        .padding(16.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.settings_outline),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(text = "Settings", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

@Composable
@Preview
fun HomeMenuPreview(){
    MenuDialog(
        show = true,
        onDismiss = {},
        onSetWallpaperClick = {}
    ){}
}