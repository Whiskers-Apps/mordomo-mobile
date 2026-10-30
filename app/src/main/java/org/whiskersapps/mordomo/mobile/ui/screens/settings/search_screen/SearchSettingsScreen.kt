package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen.SearchSettingsScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen.SearchSettingsScreenState as ScreenState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen.SearchSettingsScreenUICallback as UICallback
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen.SearchSettingsScreenVM as ScreenVM


@Composable
fun SearchSettingsScreenRoot(
    onBack: () -> Unit,
    vm: ScreenVM = koinViewModel()
) {
    LaunchedEffect(Unit) {
        vm.uiCallback.collect { uiCallback ->
            when (uiCallback) {
                UICallback.Back -> onBack()
            }
        }
    }

    SearchSettingsScreen(
        state = vm.state.collectAsState().value,
        onIntent = { vm.onIntent(it) }
    )
}

@Composable
fun SearchSettingsScreen(
    state: ScreenState,
    onIntent: (ScreenIntent) -> Unit
) {
    if (!state.loading) {
        ContainerColumn {
            Nav("Search") { onIntent(ScreenIntent.Back) }

            Column(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) { }
        }
    }
}

@Composable
@Preview
fun PreviewQuickPanelSettingsScreen() {
    MordomoTheme {
        SearchSettingsScreen(ScreenState(loading = false)) { }
    }
}