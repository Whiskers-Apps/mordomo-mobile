package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEngine
import org.whiskersapps.mordomo.mobile.core.utils.getWebsiteIconURL
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.composables.AddSearchEngineDialog
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.composables.EditSearchEngineDialog
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.buttonBubble
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.SearchEnginesScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.SearchEnginesScreenState as ScreenState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.SearchEnginesScreenVM as ScreenVM

@Composable
fun SearchEnginesScreenRoot(
    onBackClick: () -> Unit,
    vm: ScreenVM = koinViewModel()
) {
    SearchEnginesScreen(
        state = vm.state.collectAsStateWithLifecycle().value
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
fun SearchEnginesScreen(
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

    if (state.addEngineDialogState.show) {
        AddSearchEngineDialog(
            state = state.addEngineDialogState,
            onIntent = { onIntent(it) }
        )
    }

    if (state.editEngineDialogState.show) {
        EditSearchEngineDialog(
            state = state.editEngineDialogState,
            onIntent = { onIntent(it) }
        )
    }

    ContainerColumn {
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
                Nav("Search Engines") { onIntent(ScreenIntent.Back) }

                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    itemsIndexed(
                        items = state.searchEngines,
                        key = { _, engine -> engine.id }
                    ) { index, engine ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onIntent(ScreenIntent.SearchEngineClick(engine))
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            WebIcon(getWebsiteIconURL(engine.query), size = 40.dp)

                            Spacer(Modifier.width(16.dp))

                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Text(
                                    text = engine.name,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = engine.query,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(Modifier.width(8.dp))

                            if (state.defaultSearchEngineId == engine.id) {
                                Box(
                                    Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.background),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }

                        if (index < state.searchEngines.size - 1) {
                            ListDivider()
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun SearchEnginesScreenPreview() {
    MordomoTheme {
        SearchEnginesScreen(
            ScreenState(
                loading = false,
                searchEngines = listOf(
                    SearchEngine(
                        id = 0,
                        name = "DuckDuckGo",
                        query = "https:://awdawdwadawdawdawd.com"
                    ),
                    SearchEngine(
                        id = 1,
                        name = "DuckDuckGo",
                        query = "https:://awdawdwadawdawdawd.com"
                    ),
                    SearchEngine(
                        id = 2,
                        name = "DuckDuckGo",
                        query = "https:://awdawdwadawdawdawd.com"
                    ),
                    SearchEngine(
                        id = 3,
                        name = "DuckDuckGo",
                        query = "https:://awdawdwadawdawdawd.com"
                    ),
                    SearchEngine(
                        id = 4,
                        name = "DuckDuckGo",
                        query = "https:://awdawdwadawdawdawd.com"
                    ),
                ),
                defaultSearchEngineId = 1
            )
        ) {}
    }
}

