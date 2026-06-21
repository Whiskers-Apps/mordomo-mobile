package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.card
import org.whiskersapps.mordomo.mobile.ui.shared_composables.getCardShape
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
                        items = state.searchEngines,
                        key = { _, engine -> engine.id }
                    ) { index, engine ->
                        Row(
                            modifier = Modifier.card(
                                shape = getCardShape(index, state.searchEngines.size),
                                padding = 16.dp,
                                onClick = { onIntent(ScreenIntent.SearchEngineClick(engine)) }
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            WebIcon(getWebsiteIconURL(engine.query), size = 48.dp)

                            Spacer(Modifier.width(8.dp))

                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Text(
                                    engine.name,
                                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                                )

                                Spacer(Modifier.height(2.dp))

                                Text(
                                    engine.query, style = TextStyle(
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                            }

                            Spacer(Modifier.width(8.dp))

                            if (state.defaultSearchEngineId == engine.id) {
                                Box(
                                    Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.background),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground
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
@Preview
fun SearchEnginesScreenPreview() {
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

