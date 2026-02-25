package org.whiskersapps.mordomo.mobile.ui.navigation.settings.apps_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.Settings
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Apps.Companion.DEFAULT_SPLIT_LIST

data class AppsSettingsScreenState(
    val loading: Boolean = true,
    val splitList: Boolean = DEFAULT_SPLIT_LIST
)
