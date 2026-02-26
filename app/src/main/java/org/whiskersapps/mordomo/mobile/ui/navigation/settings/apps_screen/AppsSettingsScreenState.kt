package org.whiskersapps.mordomo.mobile.ui.navigation.settings.apps_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.Settings
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Apps.Companion.DEFAULT_SEARCH_BAR_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Apps.Companion.DEFAULT_SPLIT_LIST
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsOptions

data class AppsSettingsScreenState(
    val loading: Boolean = true,
    val splitList: Boolean = DEFAULT_SPLIT_LIST,
    val searchBarPosition: String = DEFAULT_SEARCH_BAR_POSITION
)
