package org.whiskersapps.mordomo.mobile.ui.screens.settings.apps_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.SegmentedOption
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.SegmentedSetting
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.SwitchSetting
import org.whiskersapps.mordomo.mobile.ui.shared_composables.BOTTOM_SHAPE
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

@Composable
fun AppsSettingsScreenRoot(
    onBackClick: () -> Unit,
    vm: AppsSettingsScreenVM = koinViewModel()
) {
    AppsSettingsScreen(vm.state.collectAsState().value) { intent ->
        when (intent) {
            AppsSettingsScreenIntent.BackClick -> onBackClick()
            else -> vm.onIntent(intent)
        }
    }
}

@Composable
fun AppsSettingsScreen(
    state: AppsSettingsScreenState,
    onIntent: (AppsSettingsScreenIntent) -> Unit
) {
    if (!state.loading) {
        ContainerColumn {
            Nav("Apps") { onIntent(AppsSettingsScreenIntent.BackClick) }

            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    .section(),
            ) {
                SwitchSetting(
                    title = "Split List",
                    description = "Split list if there's available space",
                    value = state.splitList,
                    onValueChange = { onIntent(AppsSettingsScreenIntent.SplitListClick(it)) }
                )

                ListDivider()

                SegmentedSetting(
                    shape = BOTTOM_SHAPE,
                    title = "Search Bar Position",
                    description = "Show/Hide or change the search bar position",
                    options = listOf(
                        SegmentedOption(
                            id = SettingsValues.SearchBarPosition.HIDDEN,
                            text = "Hidden"
                        ),
                        SegmentedOption(
                            id = SettingsValues.SearchBarPosition.TOP,
                            text = "Top"
                        ),
                        SegmentedOption(
                            id = SettingsValues.SearchBarPosition.BOTTOM,
                            text = "Bottom"
                        )
                    ),
                    activeOptionId = state.searchBarPosition,
                    onChange = { onIntent(AppsSettingsScreenIntent.SearchBarPositionSet(it)) }
                )
            }
        }
    }
}

@Composable
@Preview()
fun AppsSettingsScreenPreview() {
    MordomoTheme {
        AppsSettingsScreen(AppsSettingsScreenState(loading = false)) {}
    }
}