package org.whiskersapps.mordomo.mobile.ui.screens.settings.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.TextSetting
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.TextSettingDialog
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.TextSettingDialogState
import org.whiskersapps.mordomo.mobile.ui.shared_composables.BOTTOM_SHAPE
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.MIDDLE_SHAPE
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TOP_SHAPE
import org.whiskersapps.mordomo.mobile.ui.screens.settings.home_screen.HomeSettingsScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.home_screen.HomeSettingsScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.settings.home_screen.HomeSettingsScreenVM as VM

@Composable
fun HomeSettingsScreenRoot(
    onBackClick: () -> Unit,
    vm: VM = koinViewModel()
) {
    HomeSettingsScreen(vm.state.collectAsState().value) { intent ->
        when (intent) {
            Intent.BackClick -> {
                onBackClick()
            }

            else -> vm.onIntent(intent)
        }
    }
}

@Composable
fun HomeSettingsScreen(
    state: State,
    onIntent: (Intent) -> Unit
) {

    TextSettingDialog(
        state = state.dateFormatDialog,
        onValueChange = { onIntent(Intent.DateFormatType(it)) },
        onCancel = { onIntent(Intent.DateFormatDialogCancel) },
        onSave = { onIntent(Intent.DateFormatDialogSave(it)) }
    )

    ContainerColumn {
        Nav { onIntent(Intent.BackClick) }

        Spacer(Modifier.height(8.dp))

        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SegmentedSetting(
                shape = TOP_SHAPE,
                title = "Clock Style",
                description = "Change or hide the clock",
                options = listOf(
                    SegmentedOption(
                        id = SettingsValues.ClockStyle.HIDDEN,
                        text = "Hidden"
                    ),
                    SegmentedOption(
                        id = SettingsValues.ClockStyle.MINIMAL,
                        text = "Minimal"
                    ),
                    SegmentedOption(
                        id = SettingsValues.ClockStyle.ISLAND,
                        text = "Island"
                    )
                ),
                activeOptionId = state.clockStyle,
                onChange = { onIntent(Intent.ClockStyleSet(it)) }
            )

            if (state.clockStyle != SettingsValues.ClockStyle.HIDDEN) {
                SegmentedSetting(
                    shape = MIDDLE_SHAPE,
                    title = "Clock Position",
                    description = "Clock position in the home screen",
                    options = listOf(
                        SegmentedOption(
                            id = SettingsValues.ClockPosition.TOP,
                            text = "Top"
                        ),
                        SegmentedOption(
                            id = SettingsValues.ClockPosition.CENTER,
                            text = "Center"
                        )
                    ),
                    activeOptionId = state.clockPosition,
                    onChange = { onIntent(Intent.ClockPositionSet(it)) }
                )
            }

            SwitchSetting(
                shape = MIDDLE_SHAPE,
                title = "Show Date",
                description = "Show date",
                value = state.showDate,
                onValueChange = { onIntent(Intent.ShowDateClick(it)) }
            )

            if (state.showDate) {
                TextSetting(
                    shape = MIDDLE_SHAPE,
                    title = "Date Format",
                    description = "Date format under the clock",
                    text = state.dateFormat,
                    onClick = {
                        onIntent(Intent.DateFormatClick)
                    }
                )
            }

            SwitchSetting(
                shape = MIDDLE_SHAPE,
                title = "Disable Swiping",
                description = "Disable swiping on all direction in home screen",
                value = state.disableSwipe,
                onValueChange = { onIntent(Intent.DisableSwipeClick(it)) }
            )

            SwitchSetting(
                shape = MIDDLE_SHAPE,
                title = "Quick Button",
                description = "Show quick panel button",
                value = state.showQuickButton,
                onValueChange = { onIntent(Intent.ShowQuickButtonClick(it)) }
            )

            SwitchSetting(
                shape = MIDDLE_SHAPE,
                title = "Search Button",
                description = "Show search button",
                value = state.showSearchButton,
                onValueChange = { onIntent(Intent.ShowSearchButtonClick(it)) }
            )

            SwitchSetting(
                shape = BOTTOM_SHAPE,
                title = "Apps Button",
                description = "Show apps button",
                value = state.showAppsButton,
                onValueChange = { onIntent(Intent.ShowAppsButtonClick(it)) }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    HomeSettingsScreen(
        State(
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