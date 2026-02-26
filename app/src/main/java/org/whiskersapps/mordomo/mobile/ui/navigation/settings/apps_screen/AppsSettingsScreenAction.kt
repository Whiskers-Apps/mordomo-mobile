package org.whiskersapps.mordomo.mobile.ui.navigation.settings.apps_screen

sealed interface AppsSettingsScreenAction {
    data object BackClick : AppsSettingsScreenAction

    data class SplitListClick(val split: Boolean) : AppsSettingsScreenAction

    data class SearchBarPositionSet(val position: String): AppsSettingsScreenAction
}