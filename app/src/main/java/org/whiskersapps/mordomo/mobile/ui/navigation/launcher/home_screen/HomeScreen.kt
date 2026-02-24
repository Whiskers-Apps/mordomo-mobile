package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.components.Clock
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.components.HomePanel
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.components.MenuDialog
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    pagerState: PagerState,
    sheetState: SheetState,
    vm: HomeScreenVM = koinViewModel()
) {
    val scope = rememberCoroutineScope()

    HomeScreen(vm.state.collectAsState().value) { action ->
        when (action) {
            is HomeScreenAction.QuickPanelButtonClick -> {
                scope.launch {
                    pagerState.scrollToPage(0)
                }
            }

            is HomeScreenAction.AppsButtonClick -> {
                scope.launch {
                    pagerState.scrollToPage(2)
                }
            }

            HomeScreenAction.SearchButtonClick -> {
                scope.launch {
                    sheetState.expand()
                }
            }

            else -> {
                vm.onAction(action)
            }
        }
    }
}

@Composable
fun HomeScreen(state: HomeScreenState, onAction: (HomeScreenAction) -> Unit) {
    val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val statusBarSize = remember(statusBarInset) { statusBarInset + 16.dp }

    MenuDialog(
        show = state.showMenu,
        onDismiss = { onAction(HomeScreenAction.MenuDismiss) },
        onSettingsClick = { onAction(HomeScreenAction.SettingsClick) },
        onSetWallpaperClick = { onAction(HomeScreenAction.SetWallpaperClick) }
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
            ResponsiveColumn {
                Column(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {},
                            onLongClick = { onAction(HomeScreenAction.LongClick) },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .padding(24.dp)
                ) {
                    Clock(style = state.clockStyle, clock = state.clock, date = state.date)

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
                            onAction(HomeScreenAction.QuickPanelButtonClick)
                        },
                        onSearchButtonClick = {
                            onAction(HomeScreenAction.SearchButtonClick)
                        },
                        onAppsButtonClick = {
                            onAction(HomeScreenAction.AppsButtonClick)
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun Preview(){
    HomeScreen(HomeScreenState(loading = false, clock = "13:23", date = "Domingo - 14/04/2026")) { }
}