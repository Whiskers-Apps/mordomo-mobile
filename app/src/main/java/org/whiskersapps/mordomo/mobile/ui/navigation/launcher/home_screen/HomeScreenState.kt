package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.Settings

data class HomeScreenState(
    val clockStyle: String = Settings.DEFAULT_CLOCK_STYLE,
    val clock: String = "00:00",
    val date: String = ""
)
