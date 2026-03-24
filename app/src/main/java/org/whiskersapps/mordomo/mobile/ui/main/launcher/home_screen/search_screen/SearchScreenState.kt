package org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.search_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEngine


data class SearchScreenState(
    val loading: Boolean = true,
    val searchText: String = "",
    val focusSearchBar: Boolean = false,
    val apps: List<App> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
    val searchEngine: SearchEngine? = null,
    val appDialogState: AppDialogState = AppDialogState()
) {
    data class AppDialogState(
        val show: Boolean = false,
        val app: App? = null
    )
}