package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.droid.droid_icons.models.Icon
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEngine
import org.whiskersapps.mordomo.mobile.core.utils.getWebsiteIconURL
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.apps_screen.composables.AppRow
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.components.SearchBar
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.components.Section
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.components.SectionHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppDialog
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.card
import org.whiskersapps.mordomo.mobile.ui.shared_composables.getCardShape
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.SearchScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.SearchScreenState as State
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.SearchScreenUICallback as UICallback
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.SearchScreenVM as VM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenRoot(
    sheetState: SheetState,
    vm: VM = koinViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue != SheetValue.Expanded) {
            vm.onIntent(Intent.SheetClose)
        }

        if (sheetState.currentValue == SheetValue.Expanded) {
            vm.onIntent(Intent.SheetExpand)
        }
    }

    LaunchedEffect(Unit) {
        vm.uiCallbacks.onEach { callback ->
            when (callback) {
                UICallback.CloseSheet -> {
                    sheetState.hide()
                }

                UICallback.ClearFocus -> {
                    focusManager.clearFocus(true)
                }

                UICallback.FocusSearchBar -> {
                    focusRequester.requestFocus()
                }

                UICallback.HideKeyboard -> {
                    keyboardController?.hide()
                }
            }
        }.launchIn(scope)
    }

    SearchScreen(
        state = state,
        focusRequester = focusRequester
    ) {
        vm.onIntent(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: State,
    focusRequester: FocusRequester,
    onIntent: (Intent) -> Unit
) {

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

    ResponsiveColumn {
        Box(Modifier.padding(start = 8.dp, end = 8.dp)) {
            SearchBar(
                value = state.searchText,
                onValueChange = { onIntent(Intent.SearchType(it)) },
                focusRequester = focusRequester,
                onDone = { onIntent(Intent.SearchDone) }
            )
        }

        Column(
            Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.apps.isNotEmpty()) {
                SectionHeader(R.drawable.apps_outline, "Apps")

                Section(state.circleApps) {
                    state.apps.forEach { app ->
                        key("app-${app.packageName}") {
                            AppRow(
                                app = app,
                                shape = if (state.apps.size == 1) {
                                    CircleShape
                                } else {
                                    RoundedCornerShape(24.dp)
                                },
                                onClick = {
                                    onIntent(Intent.AppClick(app))
                                },
                                onLongClick = {
                                    onIntent(Intent.AppLongClick(app))
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            if (state.bookmarks.isNotEmpty()) {
                SectionHeader(R.drawable.bookmark_outline, "Bookmarks")

                Section(state.circleBookmarks) {
                    state.bookmarks.forEachIndexed { index, bookmark ->
                        key("bookmark-${bookmark.id}") {
                            Row(
                                modifier = Modifier.card(
                                    shape = getCardShape(index, state.bookmarks.size),
                                    onClick = { onIntent(Intent.BookmarkClick(bookmark)) }
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                WebIcon(getWebsiteIconURL(bookmark.url), size = 42.dp)

                                Spacer(Modifier.width(8.dp))

                                Column {
                                    Text(
                                        bookmark.name,
                                        style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                                    )

                                    Spacer(Modifier.height(2.dp))

                                    Text(
                                        bookmark.url, style = TextStyle(
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            if (state.searchEngine != null && state.searchText.isNotBlank()) {
                SectionHeader(R.drawable.globe_outline, "Web Search")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onIntent(Intent.WebSearchClick) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WebIcon(getWebsiteIconURL(state.searchEngine.query), size = 42.dp)

                    Spacer(Modifier.width(8.dp))

                    Column {
                        Text(
                            state.searchEngine.name,
                            style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = when (state.isTextURL) {
                                true -> "Open ${state.searchText}"
                                false -> "Search ${state.searchText}"
                            },
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 12.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchScreenPreview() {
    val context = LocalContext.current
    val placeholder = ContextCompat.getDrawable(context, R.drawable.logo_tie)!!

    val apps = remember {
        val temp = ArrayList<App>()
        for (i in 0..1) {
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

    SearchScreen(
        state = State(
            loading = false,
            apps = apps,
            bookmarks = listOf(
                Bookmark(id = 0, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 1, name = "Notion", url = "https://notion.so"),
            ),
            searchEngine = SearchEngine(
                id = 0,
                name = "DuckDuckGo",
                query = "awidjlawjdilawdildjawl"
            ),
            searchText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        ),
        focusRequester = remember { FocusRequester() }
    ) { }
}