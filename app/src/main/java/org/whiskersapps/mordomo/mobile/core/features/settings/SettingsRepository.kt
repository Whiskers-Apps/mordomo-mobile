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
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Apps.Companion.DEFAULT_SPLIT_LIST
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Apps.Companion.KEY_SPLIT_LIST
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Home.Companion.DEFAULT_CLOCK_STYLE
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Home.Companion.DEFAULT_DATE_FORMAT
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Home.Companion.KEY_CLOCK_STYLE
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Home.Companion.KEY_DATE_FORMAT
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Setup.Companion.DEFAULT_SETUP_COMPLETED
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Setup.Companion.KEY_SETUP_COMPLETED


val Context.dataStore by preferencesDataStore("settings")

class SettingsRepository(
    private val context: Context
) {
    private val datastore = context.dataStore

    private val scope = CoroutineScope(Dispatchers.IO)

    val settings: StateFlow<Settings> = datastore.data.map { preferences ->
        val updatedSettings = Settings(
            setup = Settings.Setup(
                completed = preferences[KEY_SETUP_COMPLETED] ?: DEFAULT_SETUP_COMPLETED
            ),
            home = Settings.Home(
                clockStyle = preferences[KEY_CLOCK_STYLE] ?: DEFAULT_CLOCK_STYLE,
                dateFormat = preferences[KEY_DATE_FORMAT] ?: DEFAULT_DATE_FORMAT
            ),
            apps = Settings.Apps(
                splitList = preferences[KEY_SPLIT_LIST] ?: DEFAULT_SPLIT_LIST
            )
        )

        updatedSettings
    }.stateIn(scope, SharingStarted.Eagerly, initialValue = Settings())

    // --------------------------------------------------------------------------- //
    fun setSetupCompleted(completed: Boolean) =
        scope.launch { datastore.edit { it[KEY_SETUP_COMPLETED] = completed } }

    suspend fun setHomeClockStyle(style: String) =
        scope.launch { datastore.edit { it[KEY_CLOCK_STYLE] = style } }

    suspend fun setDateFormat(format: String) =
        scope.launch { datastore.edit { it[KEY_DATE_FORMAT] = format } }
}