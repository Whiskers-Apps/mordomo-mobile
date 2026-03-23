package org.whiskersapps.mordomo.mobile.ui.main.settings.apps_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_SEARCH_BAR_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_SPLIT_LIST

data class AppsSettingsScreenState(
    val loading: Boolean = true,
    val splitList: Boolean = DEFAULT_SPLIT_LIST,
    val searchBarPosition: String = DEFAULT_SEARCH_BAR_POSITION
)
