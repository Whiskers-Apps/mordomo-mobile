package org.whiskersapps.mordomo.mobile.core.features.settings

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_HIDDEN_APPS
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_SEARCH_BAR_POSITION
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.DEFAULT_SPLIT_LIST
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.HiddenAppsKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.SearchBarPositionKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Apps.Companion.SecureAppsKey
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
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Quick.Companion.QuickAppsKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Search.Companion.DefaultSearchEngineKey
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Setup.Companion.DEFAULT_SETUP_COMPLETED
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.Setup.Companion.SetupCompletedKey
import org.whiskersapps.mordomo.mobile.core.features.widgets.WidgetSetting


val Context.dataStore by preferencesDataStore("settings")

class SettingsRepository(
    private val context: Context
) {
    private val datastore = context.dataStore

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _appSettings = MutableStateFlow(AppSettings())
    val appSettings = _appSettings.asStateFlow()


    init {
        datastore.data.map { preferences ->
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

                    disableSwipe = preferences[DisableSwipeKey] ?: DEFAULT_DISABLE_SWIPE,

                    widgets = Json.decodeFromString(
                        preferences[AppSettings.Home.WidgetsKey] ?: "[]"
                    )
                ),

                apps = AppSettings.Apps(
                    splitList = preferences[SplitListKey] ?: DEFAULT_SPLIT_LIST,

                    searchBarPosition = preferences[SearchBarPositionKey]
                        ?: DEFAULT_SEARCH_BAR_POSITION,

                    hiddenApps = preferences[HiddenAppsKey] ?: DEFAULT_HIDDEN_APPS,

                    secureApps = preferences[SecureAppsKey] ?: AppSettings.Apps.DEFAULT_SECURE_APPS
                ),

                search = AppSettings.Search(
                    defaultSearchEngine = preferences[DefaultSearchEngineKey]
                ),

                quick = AppSettings.Quick(
                    quickApps = preferences[QuickAppsKey]
                        ?: AppSettings.Quick.DEFAULT_QUICK_APPS,

                    quickBookmarks = preferences[AppSettings.Quick.QuickBookmarksKey]
                        ?: AppSettings.Quick.DEFAULT_QUICK_BOOKMARKS
                )
            )

            updatedAppSettings
        }.onEach { settings ->
            Log.d("Settings", settings.toString())

            _appSettings.update { settings }
            _loading.update { false }
        }.launchIn(CoroutineScope(IO))
    }

    // --------------------------------------------------------------------------- //
    suspend fun setSetupCompleted(completed: Boolean) =
        withContext(IO) { datastore.edit { it[SetupCompletedKey] = completed } }

    suspend fun setHomeClockStyle(style: String) = withContext(IO) {
        datastore.edit { it[ClockStyleKey] = style }
    }

    suspend fun setClockPosition(position: String) = withContext(IO) {
        datastore.edit { it[ClockPositionKey] = position }
    }

    suspend fun setShowDate(show: Boolean) = withContext(IO) {
        datastore.edit { it[ShowDateKey] = show }
    }

    suspend fun setDateFormat(format: String) = withContext(IO) {
        datastore.edit { it[DateFormatKey] = format }
    }

    suspend fun setShowQuickButton(show: Boolean) = withContext(IO) {
        datastore.edit { it[ShowQuickButtonKey] = show }
    }

    suspend fun setShowSearchButton(show: Boolean) = withContext(IO) {
        datastore.edit { it[ShowSearchButtonKey] = show }
    }

    suspend fun setShowAppsButton(show: Boolean) = withContext(IO) {
        datastore.edit { it[ShowAppsButtonKey] = show }
    }

    suspend fun setSplitList(split: Boolean) = withContext(IO) {
        datastore.edit { it[SplitListKey] = split }
    }

    suspend fun setSearchBarPosition(position: String) = withContext(IO) {
        datastore.edit { it[SearchBarPositionKey] = position }
    }

    suspend fun setDefaultEngineId(id: Int?) = withContext(IO) {
        when (id) {
            null -> datastore.edit { it.remove(DefaultSearchEngineKey) }
            else -> datastore.edit { it[DefaultSearchEngineKey] = id }
        }
    }

    suspend fun setDisableSwipe(disable: Boolean) = withContext(IO) {
        datastore.edit { it[DisableSwipeKey] = disable }
    }

    suspend fun setHiddenApps(hiddenApps: Set<String>) = withContext(IO) {
        datastore.edit { it[HiddenAppsKey] = hiddenApps }
    }

    suspend fun setSecureApps(secureApps: Set<String>) = withContext(IO) {
        datastore.edit { it[SecureAppsKey] = secureApps }
    }

    suspend fun setQuickApps(quickApps: Set<String>) = withContext(IO) {
        datastore.edit { it[QuickAppsKey] = quickApps }
    }

    suspend fun setQuickBookmarks(quickBookmarks: Set<String>) = withContext(IO) {
        datastore.edit { it[AppSettings.Quick.QuickBookmarksKey] = quickBookmarks }
    }

    suspend fun setWidgets(widgets: List<WidgetSetting>) = withContext(IO) {
        datastore.edit { it[AppSettings.Home.WidgetsKey] = Json.encodeToString(widgets) }
    }
}