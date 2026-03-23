package org.whiskersapps.mordomo.mobile.ui.main.settings.home_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.TextSettingDialogState

data class HomeSettingsScreenState(
    val loading: Boolean = true,

    val clockStyle: String = AppSettings.Home.DEFAULT_CLOCK_STYLE,

    val clockPosition: String = AppSettings.Home.DEFAULT_CLOCK_POSITION,

    val showDate: Boolean = AppSettings.Home.DEFAULT_SHOW_DATE,

    val dateFormat: String = AppSettings.Home.DEFAULT_DATE_FORMAT,
    val dateFormatDialog: TextSettingDialogState = TextSettingDialogState(saveEnabled = true),

    val showQuickButton: Boolean = AppSettings.Home.DEFAULT_SHOW_QUICK_BUTTON,

    val showSearchButton: Boolean = AppSettings.Home.DEFAULT_SHOW_SEARCH_BUTTON,

    val showAppsButton: Boolean = AppSettings.Home.DEFAULT_SHOW_APPS_BUTTON,
)
