package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.HomeScreenIntent as Intent

@Composable
fun Clock(
    style: String,
    clock: String,
    date: String,
    showDate: Boolean,
    onIntent: (Intent) -> Unit
) {
    when (style) {
        SettingsValues.ClockStyle.MINIMAL -> {
            val textShadow = Shadow(
                Color.Black,
                offset = Offset(2f, 2f)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.clickable {
                        onIntent(Intent.ClockClick)
                    },
                    text = clock,
                    fontSize = 64.sp,
                    color = Color.White,
                    style = TextStyle(shadow = textShadow)
                )

                if (showDate) {
                    Text(
                        modifier = Modifier.clickable {
                            onIntent(Intent.DateClick)
                        },
                        text = date,
                        fontSize = 20.sp,
                        color = Color.White,
                        style = TextStyle(shadow = textShadow)
                    )
                }
            }
        }

        SettingsValues.ClockStyle.ISLAND -> {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(40))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(top = 24.dp, bottom = 24.dp, start = 32.dp, end = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            onIntent(Intent.ClockClick)
                        },
                        text = clock,
                        fontSize = 56.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Text(
                        modifier = Modifier.clickable {
                            onIntent(Intent.DateClick)
                        },
                        text = date,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE, showBackground = true)
fun ClockPreview() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Clock(
            style = SettingsValues.ClockStyle.ISLAND,
            clock = "13:51",
            date = "Saturday - 20/06/2026",
            showDate = true,
        ) {}
    }
}