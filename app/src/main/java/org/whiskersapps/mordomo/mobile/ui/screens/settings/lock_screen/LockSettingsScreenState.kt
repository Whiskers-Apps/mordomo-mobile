package org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues

data class LockSettingsScreenState(
    val loading: Boolean = true,
    val lockMethod: String = AppSettings.Home.DEFAULT_LOCK_SCREEN_METHOD,
    val warningMessage: String? = null,
    val showAccessibilityCard: Boolean = false
)
