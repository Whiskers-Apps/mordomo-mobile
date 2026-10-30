package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables.AppsPickerState


data class SecurityScreenState(
    val showHiddenAppsPicker: Boolean = false,
    val hiddenAppsPicker: AppsPickerState = AppsPickerState(),
    val showSecureAppsPicker: Boolean = false,
    val secureAppsPicker: AppsPickerState = AppsPickerState()
)