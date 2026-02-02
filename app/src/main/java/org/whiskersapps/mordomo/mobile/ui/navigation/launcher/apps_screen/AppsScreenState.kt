package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App

data class AppsScreenState(
    val loading: Boolean = true,
    val searchText: String = "",
    val apps: List<App> = emptyList()
)
