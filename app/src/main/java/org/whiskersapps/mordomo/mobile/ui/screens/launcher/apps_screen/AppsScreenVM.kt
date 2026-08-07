package org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiskersapps.lib.Sniffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.AppsScreenState.AppDialogState
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.AppsScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.AppsScreenUICallback as UICallback

class AppsScreenVM(
    private val appsRepository: AppsRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val screenIntents = ScreenIntents()
    private val appDialogIntents = AppDialogIntents()

    private val _state = MutableStateFlow(AppsScreenState())
    val state = _state.asStateFlow()

    private val _uiCallbacks = Channel<UICallback>()
    val uiCallbacks = _uiCallbacks.receiveAsFlow()

    private val sniffer = Sniffer()

    init {
        combine(
            appsRepository.filteredApps,
            settingsRepository.appSettings
        ) { apps, settings ->
            _state.update {
                it.copy(
                    loading = false,
                    apps = getFilteredApps(apps, it.searchText),
                    splitList = settings.apps.splitList,
                    searchBarPosition = settings.apps.searchBarPosition
                )
            }
        }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: ScreenIntent) {
        when (intent) {
            is ScreenIntent.SearchType -> screenIntents.onSearch(intent.text)
            is ScreenIntent.AppClick -> screenIntents.onAppClick(intent.app)
            is ScreenIntent.AppLongClick -> screenIntents.onAppLongClick(intent.app)
            ScreenIntent.SearchDone -> screenIntents.onSearchDone()

            is AppDialogIntent -> when (intent) {
                is AppDialogIntent.InfoClick -> appDialogIntents.onInfoClick()
                is AppDialogIntent.ShortcutClick -> appDialogIntents.onShortcutClick(
                    intent.shortcut
                )

                is AppDialogIntent.UninstallClick -> appDialogIntents.onUninstallClick()
                AppDialogIntent.Close -> appDialogIntents.onClose()
            }

            is ScreenIntent.OpenLockedApp -> screenIntents.onOpenLockedApp(intent.app)
        }
    }

    private inner class ScreenIntents {
        fun onSearch(text: String) {
            _state.update { it.copy(searchText = text) }

            viewModelScope.launch {
                _state.update {
                    it.copy(
                        apps = getFilteredApps(
                            appsRepository.filteredApps.value,
                            text
                        )
                    )
                }
            }
        }

        fun onAppClick(app: App) {
            viewModelScope.launch {
                if (app.packageName in appsRepository.getSecureApps()) {
                    _uiCallbacks.send(UICallback.RequestFingerprint(app))
                    return@launch
                }

                openApp(app)
            }
        }

        fun onOpenLockedApp(app: App) {
            viewModelScope.launch {
                openApp(app)
            }
        }

        private fun openApp(app: App) {
            viewModelScope.launch {
                appsRepository.openApp(app)

                delay(500)
                _uiCallbacks.send(UICallback.ClearFocus)
                _uiCallbacks.send(UICallback.HideKeyboard)
                _uiCallbacks.send(UICallback.GoHome)

                resetScreen()
            }
        }

        fun onAppLongClick(app: App) {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        appDialogState = AppDialogState(
                            show = true,
                            app = app
                        )
                    )
                }
            }
        }

        fun onSearchDone() {
            viewModelScope.launch {
                val state = state.value

                if (state.searchText.trim().isEmpty()) {
                    resetScreen()
                    return@launch
                }

                val filteredApps = state.apps
                val app = filteredApps.getOrNull(0)

                app?.let { app ->
                    appsRepository.openApp(app)

                    delay(500)
                    _uiCallbacks.send(UICallback.ClearFocus)
                    _uiCallbacks.send(UICallback.HideKeyboard)
                    _uiCallbacks.send(UICallback.GoHome)
                }

                resetScreen()
            }
        }

        private fun resetScreen() {
            viewModelScope.launch(Dispatchers.Main) {
                _state.update {
                    AppsScreenState(loading = false, apps = appsRepository.filteredApps.value)
                }
            }
        }
    }

    private inner class AppDialogIntents {
        fun onInfoClick() {
            viewModelScope.launch {
                appsRepository.openAppInfo(state.value.appDialogState.app!!)
                closeDialog()
            }
        }

        fun onUninstallClick() {
            viewModelScope.launch {
                appsRepository.requestUninstall(state.value.appDialogState.app!!)
                closeDialog()
            }
        }

        fun onShortcutClick(shortcut: App.Shortcut) {
            viewModelScope.launch {
                appsRepository.openShortcut(state.value.appDialogState.app!!, shortcut)
                closeDialog()
            }
        }

        fun onClose() = closeDialog()

        private fun closeDialog() {
            _state.update {
                it.copy(appDialogState = AppDialogState())
            }
        }
    }

    private fun getFilteredApps(apps: List<App>, searchText: String): List<App> {
        return when (searchText.trim().isEmpty()) {
            true -> apps
            false -> apps.filter { app -> sniffer.matches(app.name, searchText) }
        }
    }
}