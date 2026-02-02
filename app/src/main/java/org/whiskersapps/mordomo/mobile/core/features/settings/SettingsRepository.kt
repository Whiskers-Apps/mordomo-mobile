package org.whiskersapps.mordomo.mobile.core.features.settings

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Companion.DEFAULT_CLOCK_STYLE
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Companion.DEFAULT_SETUP_COMPLETED
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Companion.KEY_CLOCK_STYLE
import org.whiskersapps.mordomo.mobile.core.features.settings.Settings.Companion.KEY_SETUP_COMPLETED


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
            )
        )

        updatedSettings
    }.stateIn(scope, SharingStarted.Eagerly, initialValue = Settings())

    fun setSetupCompleted(completed: Boolean) {
        scope.launch { datastore.edit { it[KEY_SETUP_COMPLETED] = completed } }
    }
}