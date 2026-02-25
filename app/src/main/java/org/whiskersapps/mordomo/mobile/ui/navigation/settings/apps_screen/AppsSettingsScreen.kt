package org.whiskersapps.mordomo.mobile.ui.navigation.settings.apps_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.SwitchSetting
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn

@Composable
fun AppsSettingsScreenRoot(
    onBackClick: () -> Unit,
    vm: AppsSettingsScreenVM = koinViewModel()
) {
    AppsSettingsScreen(vm.state.collectAsState().value) { action ->
        when (action) {
            AppsSettingsScreenAction.BackClick -> onBackClick()
            else -> vm.onAction(action)
        }
    }
}

@Composable
fun AppsSettingsScreen(
    state: AppsSettingsScreenState,
    onAction: (AppsSettingsScreenAction) -> Unit
) {
    if (!state.loading) {
        ResponsiveColumn(scrollable = true) {
            Nav { onAction(AppsSettingsScreenAction.BackClick) }

            Spacer(Modifier.height(8.dp))

            SwitchSetting(
                title = "Split List",
                description = "Split list if there's available space",
                value = state.splitList,
                onValueChange = { onAction(AppsSettingsScreenAction.SplitListClick(it)) }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AppsSettingsScreenPreview() {
    AppsSettingsScreen(AppsSettingsScreenState(loading = false)) {}
}