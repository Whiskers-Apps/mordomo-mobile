package org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.composables.Clock
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.composables.HomePanel
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.composables.MenuDialog
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import kotlin.math.abs
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.HomeScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.HomeScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.HomeScreenUICallback as UICallback
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.HomeScreenVM as VM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    onGoToQuickPanel: () -> Unit,
    onGoToApps: () -> Unit,
    onExpandSheet: () -> Unit,
    vm: VM = koinViewModel()
) {
    LaunchedEffect(Unit) {
        vm.uiCallback.collect {
            when (it) {
                UICallback.ExpandSheet -> onExpandSheet()
                UICallback.GoToApps -> onGoToApps()
                UICallback.GoToQuickPanel -> onGoToQuickPanel()
            }
        }
    }

    HomeScreen(
        vm.state.collectAsState().value,
        onIntent = { vm.onIntent(it) }
    )
}

@Composable
fun HomeScreen(
    state: State,
    onIntent: (Intent) -> Unit
) {
    val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val statusBarSize = remember(statusBarInset) { statusBarInset + 16.dp }

    MenuDialog(
        show = state.showMenu,
        onDismiss = { onIntent(Intent.MenuDismiss) },
        showLockOption = state.showLockOption,
        onSettingsClick = { onIntent(Intent.SettingsClick) },
        onSetWallpaperClick = { onIntent(Intent.SetWallpaperClick) },
        onLockScreenClick = { onIntent(Intent.LockScreenClick) }
    )

    if (state.showAccessibilityDialog) {
        DialogLayout(onDismiss = { onIntent(Intent.AccessibilityDialogClose) }) {
            DialogHeader("Accessibility")

            Text(
                text = "This screen locking method requires accessibility access. This permission is not used anywhere else by the application. If you require verification you can check the app source code that's available in the about page in settings.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )

            DialogFooter(
                onCancel = { onIntent(Intent.AccessibilityDialogClose) },
                primaryButtonText = "Open Accessibility Settings",
                onPrimaryClick = { onIntent(Intent.OpenAccessibilityClick) }
            )
        }
    }

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
            ContainerColumn(
                modifier = Modifier
                    .pointerInput(state.disableSwipe) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (abs(dragAmount) > 40) {
                                if (dragAmount < 0) {
                                    if (!state.disableSwipe) {
                                        onIntent(Intent.SwipeUp)
                                    }
                                } else {
                                    if (!state.disableSwipe) {
                                        onIntent(Intent.SwipeDown)
                                    }
                                }
                            }
                        }
                    }
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { onIntent(Intent.LongClick) },
                        onDoubleClick = { onIntent(Intent.DoubleClick) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp, bottom = 24.dp, end = 24.dp)
                ) {
                    if (state.clockPosition == SettingsValues.ClockPosition.CENTER) {
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
                        showDate = state.showDate,
                        onIntent = { onIntent(it) }
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
                            onIntent(Intent.QuickPanelButtonClick)
                        },
                        onSearchButtonClick = {
                            onIntent(Intent.SearchButtonClick)
                        },
                        onAppsButtonClick = {
                            onIntent(Intent.AppsButtonClick)
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = false, showSystemUi = true)
fun Preview() {
    HomeScreen(
        State(
            loading = false,
            clock = "13:56",
            date = "Domingo - 14/04/2026",
            showQuickButton = false,
            showSearchButton = true,
            showAppsButton = true,
            showAccessibilityDialog = true,
            showMenu = false
        )
    ) { }
}