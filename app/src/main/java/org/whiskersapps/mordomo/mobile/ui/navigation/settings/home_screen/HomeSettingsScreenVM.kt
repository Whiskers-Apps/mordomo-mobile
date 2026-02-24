package org.whiskersapps.mordomo.mobile.ui.navigation.settings.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables.TextSettingDialogState

class HomeSettingsScreenVM(val settingsRepository: SettingsRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeSettingsScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = settingsRepository.settings.value

            _state.update {
                HomeSettingsScreenState(
                    loading = false,
                    clockStyle = settings.home.clockStyle,
                    dateFormat = settings.home.dateFormat,
                    showQuickButton = settings.home.showQuickButton,
                    showSearchButton = settings.home.showSearchButton,
                    showAppsButton = settings.home.showAppsButton
                )
            }
        }
    }

    fun onAction(action: HomeSettingsScreenAction) {
        when (action) {
            is HomeSettingsScreenAction.ClockStyleSet -> onClockStyleSet(action.style)

            HomeSettingsScreenAction.DateFormatClick -> onDateFormatClick()
            is HomeSettingsScreenAction.DateFormatType -> onDateFormatType(action.format)
            is HomeSettingsScreenAction.DateFormatDialogSave -> onDateFormatDialogSave(action.value)
            HomeSettingsScreenAction.DateFormatDialogCancel -> onDateFormatDialogCancel()

            is HomeSettingsScreenAction.ShowQuickButtonClick -> onShowQuickButtonClick(action.value)

            is HomeSettingsScreenAction.ShowSearchButtonClick -> onShowSearchButtonClick(action.value)

            is HomeSettingsScreenAction.ShowAppsButtonClick -> onShowAppsButtonClick(action.value)

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