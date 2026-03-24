package org.whiskersapps.mordomo.mobile.core.features.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class AppSettings(
    val setup: Setup = Setup(),
    val home: Home = Home(),
    val apps: Apps = Apps(),
    val search: Search = Search()
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

        val clockPosition: String = DEFAULT_CLOCK_POSITION,

        val showDate: Boolean = DEFAULT_SHOW_DATE,

        val dateFormat: String = DEFAULT_DATE_FORMAT,

        val showQuickButton: Boolean = DEFAULT_SHOW_QUICK_BUTTON,

        val showSearchButton: Boolean = DEFAULT_SHOW_SEARCH_BUTTON,

        val showAppsButton: Boolean = DEFAULT_SHOW_APPS_BUTTON,
    ) {
        companion object {
            val KEY_CLOCK_STYLE = stringPreferencesKey("clock_style")
            const val DEFAULT_CLOCK_STYLE = SettingsOptions.ClockStyle.MINIMAL

            val KEY_CLOCK_POSITION = stringPreferencesKey("clock_position")
            const val DEFAULT_CLOCK_POSITION = SettingsOptions.ClockPosition.TOP

            val KEY_SHOW_DATE = booleanPreferencesKey("show_date")
            const val DEFAULT_SHOW_DATE = true

            val KEY_DATE_FORMAT = stringPreferencesKey("date_format")
            const val DEFAULT_DATE_FORMAT = "EEEE - dd/MM/Y"

            val KEY_SHOW_QUICK_BUTTON = booleanPreferencesKey("show_quick_button")
            const val DEFAULT_SHOW_QUICK_BUTTON = true

            val KEY_SHOW_SEARCH_BUTTON = booleanPreferencesKey("show_search_button")
            const val DEFAULT_SHOW_SEARCH_BUTTON = true

            val KEY_SHOW_APPS_BUTTON = booleanPreferencesKey("show_apps_button")
            const val DEFAULT_SHOW_APPS_BUTTON = false
        }
    }

    data class Apps(
        val splitList: Boolean = DEFAULT_SPLIT_LIST,
        val searchBarPosition: String = DEFAULT_SEARCH_BAR_POSITION
    ) {
        companion object {
            val KEY_SPLIT_LIST = booleanPreferencesKey("split_apps_list")
            const val DEFAULT_SPLIT_LIST = true

            val KEY_SEARCH_BAR_POSITION = stringPreferencesKey("search_bar_position")
            const val DEFAULT_SEARCH_BAR_POSITION = SettingsOptions.SearchBarPosition.BOTTOM
        }
    }

    data class Search(
        val defaultSearchEngine: Int? = DEFAULT_DEFAULT_SEARCH_ENGINE
    ) {
        companion object {
            val KEY_DEFAULT_SEARCH_ENGINE = intPreferencesKey("default_search_engine")
            val DEFAULT_DEFAULT_SEARCH_ENGINE = null
        }
    }
}


object SettingsOptions {
    object ClockStyle {
        const val MINIMAL = "minimal"
        const val ISLAND = "island"
        const val HIDDEN = "hidden"
    }

    object ClockPosition {
        const val TOP = "top"
        const val CENTER = "center"
    }

    object SearchBarPosition {
        const val TOP = "top"
        const val BOTTOM = "bottom"
        const val HIDDEN = "hidden"
    }
}


