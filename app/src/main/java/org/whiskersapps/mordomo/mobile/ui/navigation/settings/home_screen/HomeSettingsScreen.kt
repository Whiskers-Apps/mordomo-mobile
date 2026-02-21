package org.whiskersapps.mordomo.mobile.ui.navigation.settings.home_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsOptions
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.SegmentedOption
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.SegmentedSetting
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.TextSetting
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.TextSettingDialog
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn

@Composable
fun HomeSettingsScreenRoot(
    onBackClick: () -> Unit,
    vm: HomeSettingsScreenVM = koinViewModel()
) {
    HomeSettingsScreen(vm.state.collectAsState().value) { action ->
        when (action) {
            HomeSettingsScreenAction.BackClick -> {
                onBackClick()
            }

            else -> vm.onAction(action)
        }
    }
}

@Composable
fun HomeSettingsScreen(
    state: HomeSettingsScreenState,
    onAction: (HomeSettingsScreenAction) -> Unit
) {

    TextSettingDialog(
        state = state.dateFormatDialog,
        onValueChange = { onAction(HomeSettingsScreenAction.DateFormatType(it)) },
        onCancel = { onAction(HomeSettingsScreenAction.DateFormatDialogCancel) },
        onSave = {onAction(HomeSettingsScreenAction.DateFormatDialogSave(it))}
    )

    ResponsiveColumn {
        Nav { onAction(HomeSettingsScreenAction.BackClick) }

        Spacer(Modifier.height(8.dp))

        SegmentedSetting(
            title = "Clock Style",
            description = "Change or hide the clock",
            options = listOf(
                SegmentedOption(
                    id = SettingsOptions.ClockStyle.HIDDEN,
                    text = "Hidden"
                ),
                SegmentedOption(
                    id = SettingsOptions.ClockStyle.MINIMAL,
                    text = "Minimal"
                ),
                SegmentedOption(
                    id = SettingsOptions.ClockStyle.ISLAND,
                    text = "Island"
                )
            ),
            activeOptionId = state.clockStyle,
            onChange = { onAction(HomeSettingsScreenAction.ClockStyleSet(it)) }
        )

        TextSetting(
            title = "Date Format",
            description = "Date format under the clock",
            text = state.dateFormat,
            onClick = {
                onAction(HomeSettingsScreenAction.DateFormatClick)
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    HomeSettingsScreen(
        HomeSettingsScreenState(
            loading = false
        )
    ) {

    }
}