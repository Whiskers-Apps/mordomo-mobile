package org.whiskersapps.mordomo.mobile.ui.main.launcher.apps_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App

sealed interface AppsScreenIntent {
    data class SearchType(val text: String) : AppsScreenIntent
    data class AppClick(val app: App) : AppsScreenIntent
    data class AppLongClick(val app: App) : AppsScreenIntent
    data object SearchDone : AppsScreenIntent
}

sealed interface AppDialogIntent : AppsScreenIntent {
    data object InfoClick : AppDialogIntent
    data object UninstallClick : AppDialogIntent
    data class ShortcutClick(val shortcut: App.Shortcut) : AppDialogIntent
    data object Close : AppDialogIntent
}
