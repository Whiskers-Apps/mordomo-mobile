package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Apps.Companion.DEFAULT_SPLIT_LIST

data class AppsScreenState(
    val loading: Boolean = true,
    val searchText: String = "",
    val apps: List<App> = emptyList(),
    val longClickedApp: App? = null,
    val splitList: Boolean = DEFAULT_SPLIT_LIST
)
