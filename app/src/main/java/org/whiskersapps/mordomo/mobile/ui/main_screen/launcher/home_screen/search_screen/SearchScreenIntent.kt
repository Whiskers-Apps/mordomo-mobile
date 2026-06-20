package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark

sealed interface SearchScreenIntent {
    data class SearchType(val text: String) : SearchScreenIntent
    data object SearchDone : SearchScreenIntent
    data object SheetExpand : SearchScreenIntent
    data object SheetClose : SearchScreenIntent
    data object FocusClear : SearchScreenIntent

    data class AppClick(val app: App) : SearchScreenIntent
    data class AppLongClick(val app: App) : SearchScreenIntent

    data class BookmarkClick(val bookmark: Bookmark) : SearchScreenIntent

    data object WebSearchClick : SearchScreenIntent
}

sealed interface AppDialogIntent : SearchScreenIntent {
    data object InfoClick : AppDialogIntent
    data object UninstallClick : AppDialogIntent
    data class ShortcutClick(val shortcut: App.Shortcut) : AppDialogIntent
    data object Close : AppDialogIntent
}

sealed interface SearchScreenUICallback {
    data object CloseSheet : SearchScreenUICallback
    data object ClearFocus : SearchScreenUICallback
    data object FocusSearchBar : SearchScreenUICallback
    data object HideKeyboard : SearchScreenUICallback
}