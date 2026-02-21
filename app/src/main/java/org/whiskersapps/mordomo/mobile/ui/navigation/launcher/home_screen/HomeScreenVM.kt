package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.SettingsActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeScreenVM(
    val context: Application,
    val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val initialSettings = settingsRepository.settings.value

            _state.update {
                HomeScreenState(
                    loading = false,
                    clockStyle = initialSettings.home.clockStyle
                )
            }

            viewModelScope.launch {
                while (true) {
                    val calendar = Calendar.getInstance()
                    val locale = Locale.getDefault()

                    val formatter =
                        SimpleDateFormat(settingsRepository.settings.value.home.dateFormat, locale)

                    val hours = String.format(locale, "%02d", calendar.get(Calendar.HOUR_OF_DAY))
                    val minutes = String.format(locale, "%02d", calendar.get(Calendar.MINUTE))

                    val clockText = "$hours:$minutes"
                    val dateText = formatter.format(calendar.time)

                    _state.update {
                        it.copy(
                            clock = clockText,
                            date = dateText
                        )
                    }

                    delay(1000)
                }
            }

            settingsRepository.settings.onEach { settings ->
                _state.update {
                    it.copy(
                        clockStyle = settings.home.clockStyle
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onAction(action: HomeScreenAction) {
        when (action) {
            HomeScreenAction.LongClick -> onLongClick()
            HomeScreenAction.SetWallpaperClick -> onSetWallpaperClick()
            HomeScreenAction.SettingsClick -> onSettingsClick()
            HomeScreenAction.MenuDismiss -> onMenuDismiss()
            else -> {}
        }
    }

    // ---------------------------------------------------------- //

    private fun onLongClick() {
        viewModelScope.launch {
            _state.update { it.copy(showMenu = true) }
        }
    }

    private fun onSetWallpaperClick() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(showMenu = false) }

                val intent = Intent(Intent.ACTION_SET_WALLPAPER)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("Debug", "Error opening wallpaper selector. $e")
            }
        }
    }

    private fun onSettingsClick() {
        viewModelScope.launch {
            _state.update { it.copy(showMenu = false) }

            val intent = Intent(context, SettingsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private fun onMenuDismiss() {
        viewModelScope.launch {
            _state.update { it.copy(showMenu = false) }
        }
    }
}