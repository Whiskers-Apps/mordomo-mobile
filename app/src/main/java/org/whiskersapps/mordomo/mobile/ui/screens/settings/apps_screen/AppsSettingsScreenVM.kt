package org.whiskersapps.mordomo.mobile.ui.screens.settings.apps_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository

class AppsSettingsScreenVM(
    val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AppsSettingsScreenState())
    val state = _state.asStateFlow()

    init {
        val settings = settingsRepository.appSettings.value

        _state.update {
            AppsSettingsScreenState(
                loading = false,
                splitList = settings.apps.splitList,
                searchBarPosition = settings.apps.searchBarPosition
            )
        }
    }

    fun onIntent(intent: AppsSettingsScreenIntent) {
        when (intent) {
            is AppsSettingsScreenIntent.SplitListClick -> onSplitListClick(intent.split)
            is AppsSettingsScreenIntent.SearchBarPositionSet -> onSearchBarPositionSet(intent.position)
            else -> {}
        }
    }

    // ------------------------------------------------------------------- //

    private fun onSplitListClick(split: Boolean) {
        _state.update { it.copy(splitList = split) }

        viewModelScope.launch {
            settingsRepository.setSplitList(split)
        }
    }

    private fun onSearchBarPositionSet(position: String) {
        _state.update { it.copy(searchBarPosition = position) }

        viewModelScope.launch {
            settingsRepository.setSearchBarPosition(position)
        }
    }
}