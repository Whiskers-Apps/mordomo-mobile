package org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.droid.droid_icons.models.Icon
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.fingerprint.requestFingerprint
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppDialog
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.composables.AppRow
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.composables.SearchBar
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.AppsScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.AppsScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.AppsScreenUICallback as UICallback

@Composable
fun AppsScreenRoot(
    pagerState: PagerState,
    vm: AppsScreenVM = koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        vm.uiCallbacks.onEach { callback ->
            when (callback) {
                UICallback.GoHome -> {
                    pagerState.scrollToPage(1)
                }

                UICallback.HideKeyboard -> {
                    keyboardController?.hide()
                }

                UICallback.ClearFocus -> {
                    focusManager.clearFocus(true)
                }

                is UICallback.RequestFingerprint -> {
                    requestFingerprint(
                        context = context,
                        title = "Open ${callback.app.name}",
                        message = "Unlock to open this app",
                        onSuccess = { vm.onIntent(Intent.OpenLockedApp(callback.app)) },
                    )
                }
            }
        }.launchIn(scope)
    }

    AppsScreen(vm.state.collectAsState().value) { intent ->
        vm.onIntent(intent)
    }
}

@Composable
fun AppsScreen(state: State, onIntent: (Intent) -> Unit) {
    val lazyGridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    if (!state.loading) {
        if (state.appDialogState.show) {
            AppDialog(
                app = state.appDialogState.app!!,
                onDismiss = { onIntent(AppDialogIntent.Close) },
                onAppInfoClick = { onIntent(AppDialogIntent.InfoClick) },
                onUninstallClick = { onIntent(AppDialogIntent.UninstallClick) },
                onShortcutClick = { shortcut ->
                    onIntent(AppDialogIntent.ShortcutClick(shortcut))
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
                .padding(24.dp)
                .imePadding()
        ) {
            if (state.searchBarPosition == SettingsValues.SearchBarPosition.TOP) {
                SearchBar(
                    value = state.searchText,
                    onValueChange = { text -> onIntent(Intent.SearchType(text)) },
                    onDone = {
                        onIntent(Intent.SearchDone)
                    }
                )

                Spacer(Modifier.height(8.dp))
            }

            if (state.apps.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f, fill = true),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = """¯\_(ツ)_/¯""",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "No Apps Found. Verify your spelling.",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f, fill = true),
                    columns = if (state.splitList) {
                        GridCells.Adaptive(minSize = 300.dp)
                    } else {
                        GridCells.Fixed(1)
                    },
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    state = lazyGridState
                ) {
                    items(state.apps, key = { it.packageName }) { app ->
                        AppRow(
                            app,
                            onClick = {
                                onIntent(Intent.AppClick(app))

                                scope.launch(Dispatchers.Main) {
                                    lazyGridState.scrollToItem(0)
                                }
                            },
                            onLongClick = {
                                onIntent(Intent.AppLongClick(app))
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (state.searchBarPosition == SettingsValues.SearchBarPosition.BOTTOM) {
                SearchBar(
                    value = state.searchText,
                    onValueChange = { text -> onIntent(Intent.SearchType(text)) },
                    onDone = {
                        onIntent(Intent.SearchDone)
                    }
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AppsScreenPreview() {
    val context = LocalContext.current
    val placeholder = ContextCompat.getDrawable(context, R.drawable.logo_tie)!!

    val apps = remember {
        val temp = ArrayList<App>()
        for (i in 0..23) {
            temp.add(
                App(
                    name = "App $i",
                    packageName = "app $i",
                    shortcuts = listOf(),
                    icons = App.Icons(
                        stock = Icon(drawable = placeholder, adaptive = null),
                        themed = null
                    ),
                    widgets = emptyList()
                )
            )
        }

        temp
    }

    AppsScreen(
        State(
            loading = false,
            apps = apps,
            searchBarPosition = SettingsValues.SearchBarPosition.BOTTOM
        )
    ) { }
}