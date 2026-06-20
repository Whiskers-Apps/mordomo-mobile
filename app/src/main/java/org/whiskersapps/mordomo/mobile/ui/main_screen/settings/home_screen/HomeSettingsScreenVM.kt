package org.whiskersapps.mordomo.mobile.ui.main_screen.settings.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.shared_composables.TextSettingDialogState
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.home_screen.HomeSettingsScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.home_screen.HomeSettingsScreenState as State
import org.whiskersapps.mordomo.mobile.ui.main_screen.settings.home_screen.HomeSettingsScreenUICallback as UICallback

class HomeSettingsScreenVM(val settingsRepository: SettingsRepository) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _uiCallback = Channel<UICallback>()
    val uiCallback = _uiCallback.receiveAsFlow()

    init {
        viewModelScope.launch {
            val settings = settingsRepository.appSettings.value

            _state.update {
                State(
                    loading = false,
                    clockStyle = settings.home.clockStyle,
                    clockPosition = settings.home.clockPosition,
                    showDate = settings.home.showDate,
                    dateFormat = settings.home.dateFormat,
                    disableSwipe = settings.home.disableSwipe,
                    showQuickButton = settings.home.showQuickButton,
                    showSearchButton = settings.home.showSearchButton,
                    showAppsButton = settings.home.showAppsButton
                )
            }
        }
    }

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.ClockStyleSet -> onClockStyleSet(intent.style)

            is Intent.ClockPositionSet -> onClockPositionSet(intent.position)

            is Intent.ShowDateClick -> onShowDateClick(intent.value)

            Intent.DateFormatClick -> onDateFormatClick()

            is Intent.DisableSwipeClick -> onDisableSwipeClick(intent.value)

            is Intent.DateFormatType -> onDateFormatType(intent.format)

            is Intent.DateFormatDialogSave -> onDateFormatDialogSave(intent.value)

            Intent.DateFormatDialogCancel -> onDateFormatDialogCancel()

            is Intent.ShowQuickButtonClick -> onShowQuickButtonClick(intent.value)

            is Intent.ShowSearchButtonClick -> onShowSearchButtonClick(intent.value)

            is Intent.ShowAppsButtonClick -> onShowAppsButtonClick(intent.value)

            Intent.BackClick -> onBackClick()

        }
    }

    // ----------------------------------------------------------------- //

    private fun onClockStyleSet(style: String) {
        _state.update { it.copy(clockStyle = style) }

        viewModelScope.launch {
            settingsRepository.setHomeClockStyle(style)
        }
    }

    private fun onClockPositionSet(position: String) {
        _state.update { it.copy(clockPosition = position) }

        viewModelScope.launch {
            settingsRepository.setClockPosition(position)
        }
    }

    private fun onShowDateClick(value: Boolean) {
        _state.update { it.copy(showDate = value) }

        viewModelScope.launch {
            settingsRepository.setShowDate(value)
        }
    }

    private fun onDateFormatClick() {
        _state.update {
            it.copy(
                dateFormatDialog = TextSettingDialogState(
                    show = true,
                    title = "Date Format",
                    label = "Format",
                    value = state.value.dateFormat,
                    saveEnabled = true
                )
            )
        }
    }

    private fun onDisableSwipeClick(disable: Boolean){
        viewModelScope.launch {
            _state.update { it.copy(disableSwipe = disable) }

            settingsRepository.setDisableSwipe(disable)
        }
    }

    private fun onDateFormatType(format: String) {
        _state.update {
            it.copy(
                dateFormatDialog = it.dateFormatDialog.copy(
                    value = format
                )
            )
        }
    }

    private fun onDateFormatDialogSave(value: String) {
        _state.update {
            it.copy(
                dateFormat = value,
                dateFormatDialog = TextSettingDialogState()
            )
        }

        viewModelScope.launch {
            settingsRepository.setDateFormat(value)
        }
    }

    private fun onDateFormatDialogCancel() {
        _state.update {
            it.copy(
                dateFormatDialog = TextSettingDialogState()
            )
        }
    }

    private fun onShowQuickButtonClick(value: Boolean) {
        _state.update { it.copy(showQuickButton = value) }

        viewModelScope.launch {
            settingsRepository.setShowQuickButton(value)
        }
    }

    private fun onShowSearchButtonClick(value: Boolean) {
        _state.update { it.copy(showSearchButton = value) }

        viewModelScope.launch {
            settingsRepository.setShowSearchButton(value)
        }
    }

    private fun onShowAppsButtonClick(value: Boolean) {
        _state.update { it.copy(showAppsButton = value) }

        viewModelScope.launch {
            settingsRepository.setShowAppsButton(value)
        }
    }

    private fun onBackClick(){
        viewModelScope.launch { _uiCallback.send(UICallback.GoBack) }
    }
}