package org.whiskersapps.mordomo.mobile.ui.navigation.settings.home_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.Settings
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.TextSettingDialogState

data class HomeSettingsScreenState(
    val loading: Boolean = true,

    val clockStyle: String = Settings.Home.DEFAULT_CLOCK_STYLE,

    val dateFormat: String = Settings.Home.DEFAULT_DATE_FORMAT,
    val dateFormatDialog: TextSettingDialogState = TextSettingDialogState(saveEnabled = true),

    val showQuickButton: Boolean = Settings.Home.DEFAULT_SHOW_QUICK_BUTTON,

    val showSearchButton: Boolean = Settings.Home.DEFAULT_SHOW_SEARCH_BUTTON,

    val showAppsButton: Boolean = Settings.Home.DEFAULT_SHOW_APPS_BUTTON,
)
