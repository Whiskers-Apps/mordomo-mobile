package org.whiskersapps.mordomo.mobile.ui.main_screen.settings.bookmarks_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.utils.getWebsiteIconURL
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.bookmarks_screen.composables.AddBookmarkDialog
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.bookmarks_screen.composables.EditBookmarkDialog
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.card
import org.whiskersapps.mordomo.mobile.ui.shared_composables.getCardShape
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.bookmarks_screen.BookmarksScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.bookmarks_screen.BookmarksScreenState as ScreenState
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.bookmarks_screen.BookmarksScreenVM as ScreenVM

@Composable
fun BookmarksScreenRoot(
    onBackClick: () -> Unit,
    vm: ScreenVM = koinViewModel()
) {
    BookmarksScreen(
        state = vm.state.collectAsState().value
    ) { intent ->
        when (intent) {
            ScreenIntent.Back -> {
                vm.onIntent(intent)

                onBackClick()
            }

            else -> vm.onIntent(intent)
        }
    }
}

@Composable
fun BookmarksScreen(
    state: ScreenState,
    onIntent: (ScreenIntent) -> Unit
) {
    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BackHandler {
        onIntent(ScreenIntent.Back)
    }

    LaunchedEffect(state.showUndoSnack) {
        if (state.showUndoSnack) {
            scope.launch {
                val result = snackBarState.showSnackbar(
                    message = "Undo Delete?",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Long
                )

                onIntent(ScreenIntent.EditSnackResult(result))
            }
        }
    }

    if (state.addBookmarkDialogState.show) {
        AddBookmarkDialog(
            state = state.addBookmarkDialogState,
            onIntent = { onIntent(it) }
        )
    }

    if (state.editBookmarkDialogState.show) {
        EditBookmarkDialog(
            state = state.editBookmarkDialogState,
            onIntent = { onIntent(it) }
        )
    }

    ResponsiveColumn {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = { onIntent(ScreenIntent.FABClick) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.plus),
                        contentDescription = null
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(snackBarState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        actionContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        ) { scaffoldPadding ->
            Column(modifier = Modifier.padding(scaffoldPadding)) {
                Nav { onIntent(ScreenIntent.Back) }

                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    itemsIndexed(
                        items = state.bookmarks,
                        key = { _, bookmark -> bookmark.id }
                    ) { index, bookmark ->
                        Row(
                            modifier = Modifier.card(
                                shape = getCardShape(index, state.bookmarks.size),
                                padding = 16.dp,
                                onClick = { onIntent(ScreenIntent.BookmarkClick(bookmark)) }
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            WebIcon(getWebsiteIconURL(bookmark.url))

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

@Composable
@Preview(showBackground = true)
fun BookmarksScreenPreview() {
    BookmarksScreen(
        ScreenState(
            loading = false,
            bookmarks = listOf(
                Bookmark(id = 0, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 1, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 2, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 3, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 4, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 5, name = "Notion", url = "https://notion.so"),
                Bookmark(id = 6, name = "Notion", url = "https://notion.so"),
            )
        )
    ) {}
}