package org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen

import android.app.Application
import android.provider.AlarmClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.screens.settings.SettingsActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.content.Intent as AndroidIntent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.HomeScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.HomeScreenUICallback as UICallback

class HomeScreenVM(
    val context: Application,
    val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _uiCallback = Channel<UICallback>()
    val uiCallback = _uiCallback.receiveAsFlow()

    init {
        viewModelScope.launch {
            while (true) {
                val calendar = Calendar.getInstance()
                val locale = Locale.getDefault()

                val formatter =
                    SimpleDateFormat(
                        settingsRepository.appSettings.value.home.dateFormat,
                        locale
                    )

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

        settingsRepository.appSettings.onEach { settings ->
            _state.update {
                it.copy(
                    loading = false,
                    clockStyle = settings.home.clockStyle,
                    clockPosition = settings.home.clockPosition,
                    showDate = settings.home.showDate,
                    disableSwipe = settings.home.disableSwipe,
                    showQuickButton = settings.home.showQuickButton,
                    showSearchButton = settings.home.showSearchButton,
                    showAppsButton = settings.home.showAppsButton
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: Intent) {
        when (intent) {
            Intent.ClockClick -> onClockClick()
            Intent.DateClick -> onDateClick()
            Intent.LongClick -> onLongClick()
            Intent.SetWallpaperClick -> onSetWallpaperClick()
            Intent.SettingsClick -> onSettingsClick()
            Intent.MenuDismiss -> onMenuDismiss()
            Intent.SwipeDown -> onSwipeDown()
            Intent.AppsButtonClick -> onAppsButtonClick()
            Intent.QuickPanelButtonClick -> onQuickPanelButtonClick()
            Intent.SearchButtonClick -> onSearchButtonClick()
            Intent.SwipeUp -> onSwipeUp()
        }
    }

    // --------------------------------------------------------------------- //

    private fun onClockClick() {
        viewModelScope.launch {
            try {
                val intent = AndroidIntent(AlarmClock.ACTION_SHOW_ALARMS)
                intent.flags = AndroidIntent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("Home Screen", e.toString())
            }
        }
    }

    private fun onDateClick() {
        viewModelScope.launch {
            try {
                val intent = AndroidIntent(AndroidIntent.ACTION_MAIN)
                intent.addCategory(AndroidIntent.CATEGORY_APP_CALENDAR)
                intent.flags = AndroidIntent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("Home Screen", e.toString())
            }
        }
    }

    private fun onLongClick() {
        viewModelScope.launch {
            _state.update { it.copy(showMenu = true) }
        }
    }

    private fun onSetWallpaperClick() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(showMenu = false) }

                val intent = AndroidIntent(AndroidIntent.ACTION_SET_WALLPAPER)
                intent.flags = AndroidIntent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("Debug", "Error opening wallpaper selector. $e")
            }
        }
    }

    private fun onSettingsClick() {
        viewModelScope.launch {
            _state.update { it.copy(showMenu = false) }

            val intent = AndroidIntent(context, SettingsActivity::class.java)
            intent.flags = AndroidIntent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private fun onMenuDismiss() {
        viewModelScope.launch {
            _state.update { it.copy(showMenu = false) }
        }
    }

    private fun onSwipeDown() {
        viewModelScope.launch {
            try {
                Class.forName("android.app.StatusBarManager")
                    .getMethod("expandNotificationsPanel")
                    .invoke(context.getSystemService("statusbar"))
            } catch (e: Exception) {
                println("Failed to expand notifications center $e")
            }
        }
    }

    private fun onAppsButtonClick() {
        viewModelScope.launch { _uiCallback.send(UICallback.GoToApps) }
    }

    private fun onQuickPanelButtonClick() {
        viewModelScope.launch { _uiCallback.send(UICallback.GoToQuickPanel) }
    }

    private fun onSearchButtonClick() {
        viewModelScope.launch { _uiCallback.send(UICallback.ExpandSheet) }
    }

    private fun onSwipeUp() {
        viewModelScope.launch { _uiCallback.send(UICallback.ExpandSheet) }
    }
}