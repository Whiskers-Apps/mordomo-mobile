package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.components.AppRow
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.components.SearchBar

@Composable
fun AppsScreenRoot(
    pagerState: PagerState,
    vm: AppsScreenVM = koinViewModel()
) {
    val scope = rememberCoroutineScope()

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
                    vm.onAction(action)

                    delay(200)

                    scrollToHome()
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
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun clear() {
        scope.launch {
            lazyListState.scrollToItem(0)
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    fun clearKeyboard() {
        scope.launch {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }



    if (!state.loading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
                .padding(24.dp)
                .imePadding()
        ) {


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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f, fill = true),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.apps, key = { it.packageName }) { app ->
                        AppRow(
                            app,
                            onClick = {
                                onAction(AppsScreenAction.AppClick(app))
                                clear()
                            }
                        )
                    }

                }
            }

            Spacer(Modifier.height(16.dp))

            SearchBar(
                value = state.searchText,
                onValueChange = { text -> onAction(AppsScreenAction.Search(text)) },
                onDone = {
                    if (state.searchText.trim().isEmpty()) {
                        clearKeyboard()
                        return@SearchBar
                    }

                    onAction(AppsScreenAction.SearchDone)
                }
            )
        }
    }
}