package org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.search_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.droid.droid_icons.models.Icon
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.utils.getWebsiteIconURL
import org.whiskersapps.mordomo.mobile.ui.main.launcher.apps_screen.composables.AppRow
import org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.search_screen.components.SearchBar
import org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.search_screen.components.Section
import org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.search_screen.components.SectionHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppDialog
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.card
import org.whiskersapps.mordomo.mobile.ui.shared_composables.getCardShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenRoot(
    sheetState: SheetState,
    vm: SearchScreenVM = koinViewModel()
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue != SheetValue.Expanded) {
            vm.onIntent(SearchScreenIntent.SheetClose)
        }

        if (sheetState.currentValue == SheetValue.Expanded) {
            vm.onIntent(SearchScreenIntent.SheetExpand)
        }
    }

    SearchScreen(
        state = vm.state.collectAsState().value,
    ) { intent ->
        when (intent) {
            SearchScreenIntent.SearchDone -> {
                vm.onIntent(intent)

                scope.launch {
                    delay(200)
                    sheetState.hide()
                }
            }

            is SearchScreenIntent.AppClick -> {
                vm.onIntent(intent)

                scope.launch {
                    delay(200)
                    sheetState.hide()
                }
            }

            else -> vm.onIntent(intent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchScreenState,
    onIntent: (SearchScreenIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.focusSearchBar) {
        if (!state.focusSearchBar) {
            scope.launch {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        }
    }

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
                onValueChange = { onIntent(SearchScreenIntent.SearchType(it)) },
                focus = state.focusSearchBar,
                onDone = { onIntent(SearchScreenIntent.SearchDone) }
            )
        }

        Column(
            Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.apps.isNotEmpty()) {
                SectionHeader(R.drawable.apps, "Apps")

                Section {
                    state.apps.forEach { app ->
                        key("app-${app.packageName}") {
                            AppRow(
                                app = app,
                                shape = RoundedCornerShape(24.dp),
                                onClick = {
                                    onIntent(SearchScreenIntent.AppClick(app))
                                },
                                onLongClick = {
                                    onIntent(SearchScreenIntent.AppLongClick(app))
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            if (state.bookmarks.isNotEmpty()) {
                SectionHeader(R.drawable.bookmark, "Bookmarks")

                Section {
                    state.bookmarks.forEachIndexed { index, bookmark ->
                        key("bookmark-${bookmark.id}") {
                            Row(
                                modifier = Modifier.card(
                                    shape = getCardShape(index, state.bookmarks.size),
                                    onClick = { onIntent(SearchScreenIntent.BookmarkClick(bookmark)) }
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
        for (i in 0..6) {
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
        SearchScreenState(
            loading = false, apps = apps, bookmarks = listOf(
                Bookmark(id = 0, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 1, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 2, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 3, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 4, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 5, name = "Notion", url = "https://notion.so"),
            )
        )
    ) { }
}