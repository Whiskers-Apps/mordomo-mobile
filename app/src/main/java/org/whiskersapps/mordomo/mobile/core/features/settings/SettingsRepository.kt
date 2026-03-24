package org.whiskersapps.mordomo.mobile.core.features.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_SEARCH_BAR_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_SPLIT_LIST
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.KEY_SEARCH_BAR_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.KEY_SPLIT_LIST
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_CLOCK_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_CLOCK_STYLE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_DATE_FORMAT
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_APPS_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_QUICK_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_SEARCH_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.KEY_CLOCK_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.KEY_CLOCK_STYLE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.KEY_DATE_FORMAT
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.KEY_SHOW_APPS_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.KEY_SHOW_DATE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.KEY_SHOW_QUICK_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.KEY_SHOW_SEARCH_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Search.Companion.KEY_DEFAULT_SEARCH_ENGINE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Setup.Companion.DEFAULT_SETUP_COMPLETED
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Setup.Companion.KEY_SETUP_COMPLETED


val Context.dataStore by preferencesDataStore("settings")

class SettingsRepository(
    private val context: Context
) {
    private val datastore = context.dataStore

    val appSettings: StateFlow<AppSettings> = datastore.data.map { preferences ->
        val updatedAppSettings = AppSettings(
            setup = AppSettings.Setup(
                completed = preferences[KEY_SETUP_COMPLETED] ?: DEFAULT_SETUP_COMPLETED
            ),
            home = AppSettings.Home(
                clockStyle = preferences[KEY_CLOCK_STYLE] ?: DEFAULT_CLOCK_STYLE,

                clockPosition = preferences[KEY_CLOCK_POSITION] ?: DEFAULT_CLOCK_POSITION,

                showDate = preferences[KEY_SHOW_DATE]
                    ?: AppSettings.Home.DEFAULT_SHOW_DATE,

                dateFormat = preferences[KEY_DATE_FORMAT] ?: DEFAULT_DATE_FORMAT,

                showQuickButton = preferences[KEY_SHOW_QUICK_BUTTON] ?: DEFAULT_SHOW_QUICK_BUTTON,

                showSearchButton = preferences[KEY_SHOW_SEARCH_BUTTON]
                    ?: DEFAULT_SHOW_SEARCH_BUTTON,

                showAppsButton = preferences[KEY_SHOW_APPS_BUTTON] ?: DEFAULT_SHOW_APPS_BUTTON
            ),
            apps = AppSettings.Apps(
                splitList = preferences[KEY_SPLIT_LIST] ?: DEFAULT_SPLIT_LIST,

                searchBarPosition = preferences[KEY_SEARCH_BAR_POSITION]
                    ?: DEFAULT_SEARCH_BAR_POSITION
            ),
            search = AppSettings.Search(
                defaultSearchEngine = preferences[KEY_DEFAULT_SEARCH_ENGINE]
            )
        )

        updatedAppSettings
    }.stateIn(
        CoroutineScope(Dispatchers.IO),
        SharingStarted.Eagerly,
        initialValue = AppSettings()
    )

    // --------------------------------------------------------------------------- //
    suspend fun setSetupCompleted(completed: Boolean) =
        withContext(Dispatchers.IO) { datastore.edit { it[KEY_SETUP_COMPLETED] = completed } }

    suspend fun setHomeClockStyle(style: String) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_CLOCK_STYLE] = style }
    }

    suspend fun setClockPosition(position: String) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_CLOCK_POSITION] = position }
    }

    suspend fun setShowDate(show: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_SHOW_DATE] = show }
    }

    suspend fun setDateFormat(format: String) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_DATE_FORMAT] = format }
    }

    suspend fun setShowQuickButton(show: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_SHOW_QUICK_BUTTON] = show }
    }

    suspend fun setShowSearchButton(show: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_SHOW_SEARCH_BUTTON] = show }
    }

    suspend fun setShowAppsButton(show: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_SHOW_APPS_BUTTON] = show }
    }

    suspend fun setSplitList(split: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_SPLIT_LIST] = split }
    }

    suspend fun setSearchBarPosition(position: String) = withContext(Dispatchers.IO) {
        datastore.edit { it[KEY_SEARCH_BAR_POSITION] = position }
    }

    suspend fun setDefaultEngineId(id: Int?) = withContext(Dispatchers.IO) {
        when (id) {
            null -> datastore.edit { it.remove(KEY_DEFAULT_SEARCH_ENGINE) }
            else -> datastore.edit { it[KEY_DEFAULT_SEARCH_ENGINE] = id }
        }
    }
}