package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App


data class SecurityScreenState(
    val showHiddenAppsPicker: Boolean = false,
    val hiddenAppsPicker: HiddenAppsPicker = HiddenAppsPicker(),
    val showSecureAppsPicker: Boolean = false,

    ) {
    data class HiddenAppsPicker(
        val apps: List<App> = emptyList(),
        val selectedApps: Set<String> = emptySet()
    )
}