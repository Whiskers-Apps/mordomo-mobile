package org.whiskersapps.mordomo.mobile.ui.main.settings.apps_screen

sealed interface AppsSettingsScreenIntent {
    data object BackClick : AppsSettingsScreenIntent

    data class SplitListClick(val split: Boolean) : AppsSettingsScreenIntent

    data class SearchBarPositionSet(val position: String) : AppsSettingsScreenIntent
}