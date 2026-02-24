package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.Settings
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Home.Companion.DEFAULT_CLOCK_STYLE

data class HomeScreenState(
    val loading: Boolean = true,
    val clockStyle: String = DEFAULT_CLOCK_STYLE,
    val clock: String = "00:00",
    val date: String = "",
    val showMenu: Boolean = false,
    val showQuickButton: Boolean = Settings.Home.DEFAULT_SHOW_QUICK_BUTTON,
    val showSearchButton: Boolean = Settings.Home.DEFAULT_SHOW_SEARCH_BUTTON,
    val showAppsButton: Boolean = Settings.Home.DEFAULT_SHOW_APPS_BUTTON,
)
