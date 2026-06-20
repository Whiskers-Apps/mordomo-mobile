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
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.SearchBarPositionKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.SplitListKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_CLOCK_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_CLOCK_STYLE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_DATE_FORMAT
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_APPS_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_QUICK_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_SHOW_SEARCH_BUTTON
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.ClockPositionKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.ClockStyleKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DEFAULT_DISABLE_SWIPE
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DateFormatKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.DisableSwipeKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.ShowAppsButtonKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.ShowDateKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.ShowQuickButtonKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Home.Companion.ShowSearchButtonKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Search.Companion.DefaultSearchEngineKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Setup.Companion.DEFAULT_SETUP_COMPLETED
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Setup.Companion.SetupCompletedKey


val Context.dataStore by preferencesDataStore("settings")

class SettingsRepository(
    private val context: Context
) {
    private val datastore = context.dataStore

    val appSettings: StateFlow<AppSettings> = datastore.data.map { preferences ->
        val updatedAppSettings = AppSettings(
            setup = AppSettings.Setup(
                completed = preferences[SetupCompletedKey] ?: DEFAULT_SETUP_COMPLETED
            ),
            home = AppSettings.Home(
                clockStyle = preferences[ClockStyleKey] ?: DEFAULT_CLOCK_STYLE,

                clockPosition = preferences[ClockPositionKey] ?: DEFAULT_CLOCK_POSITION,

                showDate = preferences[ShowDateKey]
                    ?: AppSettings.Home.DEFAULT_SHOW_DATE,

                dateFormat = preferences[DateFormatKey] ?: DEFAULT_DATE_FORMAT,

                showQuickButton = preferences[ShowQuickButtonKey] ?: DEFAULT_SHOW_QUICK_BUTTON,

                showSearchButton = preferences[ShowSearchButtonKey]
                    ?: DEFAULT_SHOW_SEARCH_BUTTON,

                showAppsButton = preferences[ShowAppsButtonKey] ?: DEFAULT_SHOW_APPS_BUTTON,

                disableSwipe = preferences[DisableSwipeKey] ?: DEFAULT_DISABLE_SWIPE
            ),
            apps = AppSettings.Apps(
                splitList = preferences[SplitListKey] ?: DEFAULT_SPLIT_LIST,

                searchBarPosition = preferences[SearchBarPositionKey]
                    ?: DEFAULT_SEARCH_BAR_POSITION
            ),
            search = AppSettings.Search(
                defaultSearchEngine = preferences[DefaultSearchEngineKey]
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
        withContext(Dispatchers.IO) { datastore.edit { it[SetupCompletedKey] = completed } }

    suspend fun setHomeClockStyle(style: String) = withContext(Dispatchers.IO) {
        datastore.edit { it[ClockStyleKey] = style }
    }

    suspend fun setClockPosition(position: String) = withContext(Dispatchers.IO) {
        datastore.edit { it[ClockPositionKey] = position }
    }

    suspend fun setShowDate(show: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[ShowDateKey] = show }
    }

    suspend fun setDateFormat(format: String) = withContext(Dispatchers.IO) {
        datastore.edit { it[DateFormatKey] = format }
    }

    suspend fun setShowQuickButton(show: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[ShowQuickButtonKey] = show }
    }

    suspend fun setShowSearchButton(show: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[ShowSearchButtonKey] = show }
    }

    suspend fun setShowAppsButton(show: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[ShowAppsButtonKey] = show }
    }

    suspend fun setSplitList(split: Boolean) = withContext(Dispatchers.IO) {
        datastore.edit { it[SplitListKey] = split }
    }

    suspend fun setSearchBarPosition(position: String) = withContext(Dispatchers.IO) {
        datastore.edit { it[SearchBarPositionKey] = position }
    }

    suspend fun setDefaultEngineId(id: Int?) = withContext(Dispatchers.IO) {
        when (id) {
            null -> datastore.edit { it.remove(DefaultSearchEngineKey) }
            else -> datastore.edit { it[DefaultSearchEngineKey] = id }
        }
    }

    suspend fun setDisableSwipe(disable: Boolean) = withContext(Dispatchers.IO){
        datastore.edit { it[DisableSwipeKey] = disable }
    }
}