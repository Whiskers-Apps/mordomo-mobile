package org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsOptions
import org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.composables.Clock
import org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.composables.HomePanel
import org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.composables.MenuDialog
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    onGoToQuickPanel: () -> Unit,
    onGoToApps: () -> Unit,
    onExpandSheet: () -> Unit,
    vm: HomeScreenVM = koinViewModel()
) {
    HomeScreen(vm.state.collectAsState().value) { intent ->
        when (intent) {
            is HomeScreenIntent.QuickPanelButtonClick -> {
                onGoToQuickPanel()
            }

            is HomeScreenIntent.AppsButtonClick -> {
                onGoToApps()
            }

            HomeScreenIntent.SearchButtonClick -> {
                onExpandSheet()
            }

            HomeScreenIntent.SwipeUp -> {
                onExpandSheet()
            }

            else -> {
                vm.onIntent(intent)
            }
        }
    }
}

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onIntent: (HomeScreenIntent) -> Unit
) {
    val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val statusBarSize = remember(statusBarInset) { statusBarInset + 16.dp }

    MenuDialog(
        show = state.showMenu,
        onDismiss = { onIntent(HomeScreenIntent.MenuDismiss) },
        onSettingsClick = { onIntent(HomeScreenIntent.SettingsClick) },
        onSetWallpaperClick = { onIntent(HomeScreenIntent.SetWallpaperClick) }
    )

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarSize)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )

        if (!state.loading) {
            ResponsiveColumn(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (abs(dragAmount) > 40) {
                                if (dragAmount < 0) {
                                    onIntent(HomeScreenIntent.SwipeUp)
                                } else {
                                    onIntent(HomeScreenIntent.SwipeDown)
                                }
                            }

                        }
                    }
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { onIntent(HomeScreenIntent.LongClick) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp, bottom = 24.dp, end = 24.dp)
                ) {
                    if (state.clockPosition == SettingsOptions.ClockPosition.CENTER) {
                        Spacer(
                            Modifier
                                .fillMaxHeight()
                                .weight(1f, fill = true)
                        )
                    }

                    Clock(
                        style = state.clockStyle,
                        clock = state.clock,
                        date = state.date,
                        showDate = state.showDate
                    )

                    Spacer(
                        Modifier
                            .fillMaxHeight()
                            .weight(1f, fill = true)
                    )

                    HomePanel(
                        showQuickButton = state.showQuickButton,
                        showSearchButton = state.showSearchButton,
                        showAppsButton = state.showAppsButton,
                        onQuickPanelButtonClick = {
                            onIntent(HomeScreenIntent.QuickPanelButtonClick)
                        },
                        onSearchButtonClick = {
                            onIntent(HomeScreenIntent.SearchButtonClick)
                        },
                        onAppsButtonClick = {
                            onIntent(HomeScreenIntent.AppsButtonClick)
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun Preview() {
    HomeScreen(HomeScreenState(loading = false, clock = "13:23", date = "Domingo - 14/04/2026")) { }
}