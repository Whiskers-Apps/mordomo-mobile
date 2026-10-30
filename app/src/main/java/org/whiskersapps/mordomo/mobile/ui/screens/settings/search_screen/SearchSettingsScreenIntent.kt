package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen

sealed interface SearchSettingsScreenIntent {
    data object Back : SearchSettingsScreenIntent
}

sealed interface SearchSettingsScreenUICallback {
    data object Back : SearchSettingsScreenUICallback
}