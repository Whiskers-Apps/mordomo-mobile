package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App

sealed interface AppsScreenAction {
    data class Search(val text: String) : AppsScreenAction
    data class AppClick(val app: App) : AppsScreenAction
    data object SearchDone : AppsScreenAction
}