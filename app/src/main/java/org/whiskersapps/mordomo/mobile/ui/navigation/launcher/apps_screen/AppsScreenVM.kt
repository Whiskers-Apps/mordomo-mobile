package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiskersapps.lib.Sniffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository

class AppsScreenVM(
    private val appsRepository: AppsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AppsScreenState())
    val state = _state.asStateFlow()

    private val sniffer = Sniffer()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val apps = appsRepository.filteredApps.value

            _state.update {
                it.copy(
                    loading = false,
                    apps = apps
                )
            }

            appsRepository.filteredApps.onEach { apps ->
                _state.update {
                    it.copy(apps = apps)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onAction(action: AppsScreenAction) {
        when (action) {
            is AppsScreenAction.Search -> onSearch(action.text)
            is AppsScreenAction.AppClick -> onAppClick(action.app)
            AppsScreenAction.SearchDone -> onSearchDone()
        }
    }

    // ---------------------------------------------------- //

    private fun onSearch(text: String) {
        _state.update { it.copy(searchText = text) }

        viewModelScope.launch(Dispatchers.Default) {
            val filteredApps = if (text.trim().isEmpty()) {
                appsRepository.filteredApps.value
            } else {
                appsRepository.filteredApps.value
                    .filter { app -> sniffer.matches(app.name, text) }
            }

            _state.update { it.copy(apps = filteredApps) }
        }
    }

    private fun onAppClick(app: App) {
        viewModelScope.launch {
            appsRepository.openApp(app)
            clearScreen()
        }
    }

    private fun onSearchDone() {
        viewModelScope.launch {
            val state = state.value

            if (state.searchText.trim().isEmpty()) {
                clearScreen()
                return@launch
            }

            val filteredApps = state.apps
            val app = filteredApps.getOrNull(0)

            app?.let { app ->
                appsRepository.openApp(app)
            }

            clearScreen()
        }
    }

    // ----------------------------------------------- //

    private fun clearScreen() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                it.copy(
                    apps = appsRepository.filteredApps.value,
                    searchText = ""
                )
            }
        }
    }
}