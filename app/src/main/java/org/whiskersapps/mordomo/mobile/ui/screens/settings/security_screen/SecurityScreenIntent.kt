package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

sealed interface SecurityScreenIntent {
    data object BackClick : SecurityScreenIntent
    data object HiddenAppsClick : SecurityScreenIntent
    data object SecureAppsClick : SecurityScreenIntent
}

sealed interface HiddenAppsIntent : SecurityScreenIntent {
    data object Close : HiddenAppsIntent
    data class AppClick(val packageName: String) : HiddenAppsIntent
    data object SaveClick : HiddenAppsIntent
}

sealed interface SecurityScreenUICallback {
    data object GoBack : SecurityScreenUICallback
}