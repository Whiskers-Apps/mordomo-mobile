package org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_CLOCK_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_CLOCK_STYLE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_DISABLE_SWIPE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_APPS_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_DATE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_QUICK_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_SEARCH_BUTTON

data class HomeScreenState(
    val loading: Boolean = true,
    val clockStyle: String = DEFAULT_CLOCK_STYLE,
    val clockPosition: String = DEFAULT_CLOCK_POSITION,
    val showDate: Boolean = DEFAULT_SHOW_DATE,
    val clock: String = "00:00",
    val date: String = "",
    val showMenu: Boolean = false,
    val disableSwipe: Boolean = DEFAULT_DISABLE_SWIPE,
    val showQuickButton: Boolean = DEFAULT_SHOW_QUICK_BUTTON,
    val showSearchButton: Boolean = DEFAULT_SHOW_SEARCH_BUTTON,
    val showAppsButton: Boolean = DEFAULT_SHOW_APPS_BUTTON,
)
