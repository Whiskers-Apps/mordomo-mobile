package org.whiskersapps.mordomo.mobile.core.features.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class Settings(
    val setup: Setup = Setup(),
    val home: Home = Home(),
    val apps: Apps = Apps()
) {
    data class Setup(
        val completed: Boolean = DEFAULT_SETUP_COMPLETED
    ) {
        companion object {
            val KEY_SETUP_COMPLETED = booleanPreferencesKey("setup_completed")
            const val DEFAULT_SETUP_COMPLETED = false
        }
    }

    data class Home(
        val clockStyle: String = DEFAULT_CLOCK_STYLE,
        val dateFormat: String = DEFAULT_DATE_FORMAT,
        val showQuickButton: Boolean = DEFAULT_SHOW_QUICK_BUTTON,
        val showSearchButton: Boolean = DEFAULT_SHOW_SEARCH_BUTTON,
        val showAppsButton: Boolean = DEFAULT_SHOW_APPS_BUTTON,
    ) {
        companion object {
            val KEY_CLOCK_STYLE = stringPreferencesKey("clock_style")
            const val DEFAULT_CLOCK_STYLE = SettingsOptions.ClockStyle.MINIMAL

            val KEY_DATE_FORMAT = stringPreferencesKey("date_format")
            const val DEFAULT_DATE_FORMAT = "EEEE - dd/MM/yyyy"

            val KEY_SHOW_QUICK_BUTTON = booleanPreferencesKey("show_quick_button")
            const val DEFAULT_SHOW_QUICK_BUTTON = true

            val KEY_SHOW_SEARCH_BUTTON = booleanPreferencesKey("show_search_button")
            const val DEFAULT_SHOW_SEARCH_BUTTON = true

            val KEY_SHOW_APPS_BUTTON = booleanPreferencesKey("show_apps_button")
            const val DEFAULT_SHOW_APPS_BUTTON = false
        }
    }

    data class Apps(
        val splitList: Boolean = DEFAULT_SPLIT_LIST
    ) {
        companion object {
            val KEY_SPLIT_LIST = booleanPreferencesKey("split_apps_list")
            const val DEFAULT_SPLIT_LIST = true
        }
    }

}


object SettingsOptions {
    object ClockStyle {
        const val MINIMAL = "minimal"
        const val ISLAND = "island"
        const val HIDDEN = "hidden"
    }
}


