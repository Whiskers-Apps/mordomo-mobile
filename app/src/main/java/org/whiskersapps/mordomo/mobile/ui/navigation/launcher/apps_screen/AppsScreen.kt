package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen

import android.graphics.drawable.ColorDrawable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.lighttigerxiv.layout_scaffold.isExpandedWidth
import com.lighttigerxiv.layout_scaffold.isMediumWidth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.droid.droid_icons.models.Icon
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsOptions
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.components.AppDialog
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.components.AppRow
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.components.SearchBar
import org.whiskersapps.mordomo.mobile.ui.shared_composables.hideKeyboard

@Composable
fun AppsScreenRoot(
    pagerState: PagerState,
    vm: AppsScreenVM = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun scrollToHome() {
        scope.launch {
            pagerState.scrollToPage(1)
        }
    }

    AppsScreen(vm.state.collectAsState().value) { action ->
        when (action) {
            is AppsScreenAction.AppClick -> {
                vm.onAction(action)
                scrollToHome()
            }

            AppsScreenAction.SearchDone -> {
                scope.launch {
                    hideKeyboard(focusManager, keyboardController)

                    if (vm.state.value.apps.isNotEmpty()
                        && vm.state.value.searchText.trim().isNotEmpty()
                    ) {
                        scrollToHome()
                    }

                    vm.onAction(action)
                }
            }

            else -> {
                vm.onAction(action)
            }
        }
    }
}

@Composable
fun AppsScreen(state: AppsScreenState, onAction: (AppsScreenAction) -> Unit) {
    val lazyGridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    if (!state.loading) {
        AppDialog(
            app = state.longClickedApp,
            onDismiss = { onAction(AppsScreenAction.AppDialogClose) },
            onAppInfoClick = { onAction(AppsScreenAction.AppInfoClick(it)) },
            onUninstallClick = { onAction(AppsScreenAction.UninstallAppClick(it)) },
            onShortcutClick = { app, shortcut ->
                onAction(AppsScreenAction.ShortcutClick(app, shortcut))
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
                .padding(24.dp)
                .imePadding()
        ) {
            if (state.searchBarPosition == SettingsOptions.SearchBarPosition.TOP) {
                SearchBar(
                    value = state.searchText,
                    onValueChange = { text -> onAction(AppsScreenAction.Search(text)) },
                    onDone = {
                        onAction(AppsScreenAction.SearchDone)
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
                                onAction(AppsScreenAction.AppClick(app))

                                scope.launch(Dispatchers.Main) {
                                    lazyGridState.scrollToItem(0)
                                }
                            },
                            onLongClick = {
                                onAction(AppsScreenAction.AppLongClick(app))
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (state.searchBarPosition == SettingsOptions.SearchBarPosition.BOTTOM) {
                SearchBar(
                    value = state.searchText,
                    onValueChange = { text -> onAction(AppsScreenAction.Search(text)) },
                    onDone = {
                        onAction(AppsScreenAction.SearchDone)
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
                    )
                )
            )
        }

        temp
    }

    AppsScreen(
        AppsScreenState(
            loading = false,
            apps = apps,
            searchBarPosition = SettingsOptions.SearchBarPosition.BOTTOM
        )
    ) { }
}