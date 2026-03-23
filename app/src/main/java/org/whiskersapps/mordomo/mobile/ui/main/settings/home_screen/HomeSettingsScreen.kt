package org.whiskersapps.mordomo.mobile.ui.main.settings.home_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsOptions
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.SegmentedOption
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.SegmentedSetting
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.SwitchSetting
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.TextSetting
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.TextSettingDialog
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.TextSettingDialogState
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn

@Composable
fun HomeSettingsScreenRoot(
    onBackClick: () -> Unit,
    vm: HomeSettingsScreenVM = koinViewModel()
) {
    HomeSettingsScreen(vm.state.collectAsState().value) { intent ->
        when (intent) {
            HomeSettingsScreenIntent.BackClick -> {
                onBackClick()
            }

            else -> vm.onIntent(intent)
        }
    }
}

@Composable
fun HomeSettingsScreen(
    state: HomeSettingsScreenState,
    onIntent: (HomeSettingsScreenIntent) -> Unit
) {

    TextSettingDialog(
        state = state.dateFormatDialog,
        onValueChange = { onIntent(HomeSettingsScreenIntent.DateFormatType(it)) },
        onCancel = { onIntent(HomeSettingsScreenIntent.DateFormatDialogCancel) },
        onSave = { onIntent(HomeSettingsScreenIntent.DateFormatDialogSave(it)) }
    )

    ResponsiveColumn {
        Nav { onIntent(HomeSettingsScreenIntent.BackClick) }

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
            onChange = { onIntent(HomeSettingsScreenIntent.ClockStyleSet(it)) }
        )

        if (state.clockStyle != SettingsOptions.ClockStyle.HIDDEN) {
            SegmentedSetting(
                title = "Clock Position",
                description = "Clock position in the home screen",
                options = listOf(
                    SegmentedOption(
                        id = SettingsOptions.ClockPosition.TOP,
                        text = "Top"
                    ),
                    SegmentedOption(
                        id = SettingsOptions.ClockPosition.CENTER,
                        text = "Center"
                    )
                ),
                activeOptionId = state.clockPosition,
                onChange = { onIntent(HomeSettingsScreenIntent.ClockPositionSet(it)) }
            )
        }

        SwitchSetting(
            title = "Show Date",
            description = "Show date",
            value = state.showDate,
            onValueChange = { onIntent(HomeSettingsScreenIntent.ShowDateClick(it)) }
        )

        if (state.showDate) {
            TextSetting(
                title = "Date Format",
                description = "Date format under the clock",
                text = state.dateFormat,
                onClick = {
                    onIntent(HomeSettingsScreenIntent.DateFormatClick)
                }
            )
        }

        SwitchSetting(
            title = "Quick Button",
            description = "Show quick panel button",
            value = state.showQuickButton,
            onValueChange = { onIntent(HomeSettingsScreenIntent.ShowQuickButtonClick(it)) }
        )

        SwitchSetting(
            title = "Search Button",
            description = "Show search button",
            value = state.showSearchButton,
            onValueChange = { onIntent(HomeSettingsScreenIntent.ShowSearchButtonClick(it)) }
        )

        SwitchSetting(
            title = "Apps Button",
            description = "Show apps button",
            value = state.showAppsButton,
            onValueChange = { onIntent(HomeSettingsScreenIntent.ShowAppsButtonClick(it)) }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    HomeSettingsScreen(
        HomeSettingsScreenState(
            loading = false,
            dateFormatDialog = TextSettingDialogState(
                show = false,
                title = "Date Format",
                label = "Format"
            )
        )
    ) {

    }
}