package org.whiskersapps.mordomo.mobile.ui.main.launcher.apps_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_SEARCH_BAR_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_SPLIT_LIST

data class AppsScreenState(
    val loading: Boolean = true,
    val searchText: String = "",
    val apps: List<App> = emptyList(),
    val splitList: Boolean = DEFAULT_SPLIT_LIST,
    val searchBarPosition: String = DEFAULT_SEARCH_BAR_POSITION,
    val appDialogState: AppDialogState = AppDialogState()
) {
    data class AppDialogState(
        val show: Boolean = false,
        val app: App? = null
    )
}
