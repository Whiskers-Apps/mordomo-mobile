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
            val initialSettings = settingsRepository.settings.value

            _state.update {
                it.copy(
                    loading = false,
                    apps = apps,
                    splitList = initialSettings.apps.splitList,
                    searchBarPosition = initialSettings.apps.searchBarPosition
                )
            }

            appsRepository.filteredApps.onEach { apps ->
                _state.update {
                    it.copy(apps = apps)
                }
            }.launchIn(viewModelScope)

            settingsRepository.settings.onEach { settings ->
                _state.update {
                    it.copy(
                        splitList = settings.apps.splitList,
                        searchBarPosition = settings.apps.searchBarPosition
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onAction(action: AppsScreenAction) {
        when (action) {
            is AppsScreenAction.Search -> onSearch(action.text)
            is AppsScreenAction.AppClick -> onAppClick(action.app)
            is AppsScreenAction.AppLongClick -> onAppLongClick(action.app)
            AppsScreenAction.SearchDone -> onSearchDone()
            AppsScreenAction.AppDialogClose -> onAppDialogClose()
            is AppsScreenAction.AppInfoClick -> onAppInfoClick(action.app)
            is AppsScreenAction.UninstallAppClick -> onUninstallAppClick(action.app)
            is AppsScreenAction.ShortcutClick -> onShortcutClick(action.app, action.shortcut)
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

    private fun onAppLongClick(app: App) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    longClickedApp = app
                )
            }
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

    private fun onAppDialogClose() {
        viewModelScope.launch {
            _state.update { it.copy(longClickedApp = null) }
        }
    }

    private fun onAppInfoClick(app: App) {
        viewModelScope.launch {
            appsRepository.openAppInfo(app)
            closeAppDialog()
        }
    }

    private fun onUninstallAppClick(app: App) {
        viewModelScope.launch {
            appsRepository.requestUninstall(app)
            closeAppDialog()
        }
    }

    private fun onShortcutClick(app: App, shortcut: App.Shortcut) {
        viewModelScope.launch {
            appsRepository.openShortcut(app, shortcut)
            closeAppDialog()
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

    private fun closeAppDialog() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                it.copy(longClickedApp = null)
            }
        }
    }
}