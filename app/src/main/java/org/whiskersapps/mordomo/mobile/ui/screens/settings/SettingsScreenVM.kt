package org.whiskersapps.mordomo.mobile.ui.screens.settings

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class SettingsScreenVM(
    val context: Application
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                SettingsScreenState(
                    loading = false,
                    isDefaultLauncher = isDefaultLauncher()
                )
            }

            while (true) {
                _state.update { it.copy(isDefaultLauncher = isDefaultLauncher()) }
                delay(3.seconds)
            }
        }
    }

    fun onIntent(intent: SettingsScreenIntent) {
        when (intent) {
            SettingsScreenIntent.SetDefaultLauncherClick -> onSetDefaultLauncherClick()
            else -> {}
        }
    }

    // -------------------------------------------- //
    private suspend fun isDefaultLauncher(): Boolean = withContext(Dispatchers.Default) {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }

        val resolve =
            context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

        resolve?.activityInfo?.packageName == context.packageName
    }

    private fun onSetDefaultLauncherClick() {
        viewModelScope.launch {
            val intent = Intent(Settings.ACTION_HOME_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}