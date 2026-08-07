package org.whiskersapps.mordomo.mobile.core.features.settings

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

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
            val SetupCompletedKey = booleanPreferencesKey("setup_completed")
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
        val disableSwipe: Boolean = DEFAULT_DISABLE_SWIPE
    ) {
        companion object {
            val ClockStyleKey = stringPreferencesKey("clock_style")
            const val DEFAULT_CLOCK_STYLE = SettingsValues.ClockStyle.MINIMAL

            val ClockPositionKey = stringPreferencesKey("clock_position")
            const val DEFAULT_CLOCK_POSITION = SettingsValues.ClockPosition.TOP

            val ShowDateKey = booleanPreferencesKey("show_date")
            const val DEFAULT_SHOW_DATE = true

            val DateFormatKey = stringPreferencesKey("date_format")
            const val DEFAULT_DATE_FORMAT = "EEEE - dd/MM/Y"

            val ShowQuickButtonKey = booleanPreferencesKey("show_quick_button")
            const val DEFAULT_SHOW_QUICK_BUTTON = true

            val ShowSearchButtonKey = booleanPreferencesKey("show_search_button")
            const val DEFAULT_SHOW_SEARCH_BUTTON = true

            val ShowAppsButtonKey = booleanPreferencesKey("show_apps_button")
            const val DEFAULT_SHOW_APPS_BUTTON = false

            val DisableSwipeKey = booleanPreferencesKey("disable_swipe")
            const val DEFAULT_DISABLE_SWIPE = false
        }
    }

    data class Apps(
        val splitList: Boolean = DEFAULT_SPLIT_LIST,
        val searchBarPosition: String = DEFAULT_SEARCH_BAR_POSITION,
        val hiddenApps: Set<String> = DEFAULT_HIDDEN_APPS
    ) {
        companion object {
            val SplitListKey = booleanPreferencesKey("split_apps_list")
            const val DEFAULT_SPLIT_LIST = true

            val SearchBarPositionKey = stringPreferencesKey("search_bar_position")
            const val DEFAULT_SEARCH_BAR_POSITION = SettingsValues.SearchBarPosition.BOTTOM

            val HiddenAppsKey = stringSetPreferencesKey("hidden-apps")
            val DEFAULT_HIDDEN_APPS = emptySet<String>()
        }
    }

    data class Search(
        val defaultSearchEngine: Int? = DEFAULT_SEARCH_ENGINE
    ) {
        companion object {
            val DefaultSearchEngineKey = intPreferencesKey("default_search_engine")
            val DEFAULT_SEARCH_ENGINE = null
        }
    }
}


object SettingsValues {
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


