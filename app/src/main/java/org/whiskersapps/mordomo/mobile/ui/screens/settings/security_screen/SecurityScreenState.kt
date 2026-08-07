package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App


data class SecurityScreenState(
    val showHiddenAppsPicker: Boolean = false,
    val hiddenAppsPicker: Picker = Picker(),
    val showSecureAppsPicker: Boolean = false,
    val secureAppsPicker: Picker = Picker()
) {
    data class Picker(
        val apps: List<App> = emptyList(),
        val selectedApps: Set<String> = emptySet()
    )
}