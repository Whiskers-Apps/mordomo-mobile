package org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
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
import org.whiskersapps.mordomo.mobile.core.features.fingerprint.requestFingerprint
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEngine
import org.whiskersapps.mordomo.mobile.core.utils.getWebsiteIconURL
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.composables.AppRow
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.components.SearchBar
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.components.Section
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.components.SectionHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppDialog
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.SINGLE_SHAPE
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.bubbleShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.buttonBubble
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.SearchScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.SearchScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.SearchScreenUICallback as UICallback
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.SearchScreenVM as VM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenRoot(
    sheetState: SheetState,
    vm: VM = koinViewModel()
) {
    val context = LocalContext.current
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

    ContainerColumn(contentPadded = true) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = state.searchText,
            onValueChange = { onIntent(Intent.SearchType(it)) },
            placeholder = {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            singleLine = true,
            shape = CircleShape,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            leadingIcon = {
                androidx.compose.material3.Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            trailingIcon = {
                if (state.searchText.isNotEmpty()) {
                    androidx.compose.material3.Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onIntent(Intent.SearchType(""))
                            },
                        painter = painterResource(R.drawable.clear_circle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onIntent(Intent.SearchDone) }
            )
        )

        Spacer(Modifier.height(16.dp))

        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            if (state.apps.isNotEmpty()) {
                SectionHeader("Apps")

                Column(Modifier.section()) {
                    state.apps.forEachIndexed { index, app ->
                        key("app-${app.packageName}") {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .bubbleShape(index, state.apps.size)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                AppRow(
                                    app = app,
                                    onClick = {
                                        onIntent(Intent.AppClick(app))
                                    },
                                    onLongClick = {
                                        onIntent(Intent.AppLongClick(app))
                                    },
                                    padding = PaddingValues(16.dp),
                                    size = 40.dp
                                )
                            }

                            if (index < state.apps.size - 1) {
                                ListDivider()
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            if (state.bookmarks.isNotEmpty()) {
                SectionHeader("Bookmarks")

                Column(Modifier.section()) {

                    state.bookmarks.forEachIndexed { index, bookmark ->
                        key("bookmark-${bookmark.id}") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onIntent(Intent.BookmarkClick(bookmark))
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                WebIcon(getWebsiteIconURL(bookmark.url), size = 40.dp)

                                Spacer(Modifier.width(8.dp))

                                Column {
                                    Text(
                                        text = bookmark.name,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = bookmark.url,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            if (index < state.bookmarks.size - 1) {
                                ListDivider()
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            if (state.searchEngine != null && state.searchText.isNotBlank()) {
                SectionHeader("Web Search")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .section()
                        .clickable { onIntent(Intent.WebSearchClick) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WebIcon(getWebsiteIconURL(state.searchEngine.query), size = 40.dp)

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
                    ),
                    widgets = emptyList()
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