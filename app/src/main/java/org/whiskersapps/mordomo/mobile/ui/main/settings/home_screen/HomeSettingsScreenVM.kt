package org.whiskersapps.mordomo.mobile.ui.main.settings.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.main.settings.shared_composables.TextSettingDialogState

class HomeSettingsScreenVM(val settingsRepository: SettingsRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeSettingsScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = settingsRepository.appSettings.value

            _state.update {
                HomeSettingsScreenState(
                    loading = false,
                    clockStyle = settings.home.clockStyle,
                    clockPosition = settings.home.clockPosition,
                    showDate = settings.home.showDate,
                    dateFormat = settings.home.dateFormat,
                    showQuickButton = settings.home.showQuickButton,
                    showSearchButton = settings.home.showSearchButton,
                    showAppsButton = settings.home.showAppsButton
                )
            }
        }
    }

    fun onIntent(intent: HomeSettingsScreenIntent) {
        when (intent) {
            is HomeSettingsScreenIntent.ClockStyleSet -> onClockStyleSet(intent.style)

            is HomeSettingsScreenIntent.ClockPositionSet -> onClockPositionSet(intent.position)

            is HomeSettingsScreenIntent.ShowDateClick -> onShowDateClick(intent.value)

            HomeSettingsScreenIntent.DateFormatClick -> onDateFormatClick()
            is HomeSettingsScreenIntent.DateFormatType -> onDateFormatType(intent.format)
            is HomeSettingsScreenIntent.DateFormatDialogSave -> onDateFormatDialogSave(intent.value)
            HomeSettingsScreenIntent.DateFormatDialogCancel -> onDateFormatDialogCancel()

            is HomeSettingsScreenIntent.ShowQuickButtonClick -> onShowQuickButtonClick(intent.value)

            is HomeSettingsScreenIntent.ShowSearchButtonClick -> onShowSearchButtonClick(intent.value)

            is HomeSettingsScreenIntent.ShowAppsButtonClick -> onShowAppsButtonClick(intent.value)

            else -> {}
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
}