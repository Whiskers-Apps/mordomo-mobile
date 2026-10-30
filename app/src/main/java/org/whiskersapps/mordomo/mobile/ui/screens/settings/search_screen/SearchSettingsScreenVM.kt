package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen.SearchSettingsScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen.SearchSettingsScreenState as ScreenState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen.SearchSettingsScreenUICallback as UICallback

class SearchSettingsScreenVM(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    private val _uiCallback = Channel<UICallback>()
    val uiCallback = _uiCallback.receiveAsFlow()

    init {
        settingsRepository.appSettings.onEach { settings ->
            _state.update { it.copy(loading = false) }
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: ScreenIntent) {
        when (intent) {
            ScreenIntent.Back -> onBack()
        }
    }

    private fun onBack() {
        viewModelScope.launch { _uiCallback.send(UICallback.Back) }
    }
}