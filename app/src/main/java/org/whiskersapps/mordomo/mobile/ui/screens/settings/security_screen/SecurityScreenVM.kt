package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenState.Picker
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenUICallback as UICallback

class SecurityScreenVM(
    val appsRepository: AppsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _uiCallback = Channel<UICallback>()
    val uiCallback = _uiCallback.receiveAsFlow()

    fun onIntent(intent: Intent) {
        when (intent) {
            Intent.BackClick -> onBackClick()
            Intent.HiddenAppsClick -> onHiddenAppsClick()
            Intent.SecureAppsClick -> onSecureAppsClick()

            is HiddenAppsIntent -> when (intent) {
                is HiddenAppsIntent.AppClick -> onHiddenAppClick(intent.packageName)
                HiddenAppsIntent.Close -> onHiddenAppsClose()
                HiddenAppsIntent.SaveClick -> onHiddenAppsSave()
            }

            is SecureAppsIntent -> when (intent) {
                is SecureAppsIntent.AppClick -> onSecureAppClick(intent.packageName)
                SecureAppsIntent.SaveClick -> onSecureAppsSave()
                SecureAppsIntent.Close -> onSecureAppsClose()
            }

        }
    }

    private fun onBackClick() {
        viewModelScope.launch { _uiCallback.send(UICallback.GoBack) }
    }

    private fun onHiddenAppsClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showHiddenAppsPicker = true,
                    hiddenAppsPicker = Picker(
                        apps = appsRepository.apps,
                        selectedApps = appsRepository.getHiddenApps()
                    )
                )
            }
        }
    }

    private fun onSecureAppsClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showSecureAppsPicker = true,
                    secureAppsPicker = Picker(
                        apps = appsRepository.apps,
                        selectedApps = appsRepository.getSecureApps()
                    )
                )
            }
        }
    }

    private fun onHiddenAppClick(packageName: String) {
        viewModelScope.launch {

            val selectedApps = state.value.hiddenAppsPicker.selectedApps.toMutableSet()

            if (packageName in selectedApps) {
                selectedApps.remove(packageName)
            } else {
                selectedApps.add(packageName)
            }

            _state.update {
                it.copy(
                    hiddenAppsPicker = it.hiddenAppsPicker.copy(
                        selectedApps = selectedApps
                    )
                )
            }
        }
    }

    private fun onHiddenAppsClose() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showHiddenAppsPicker = false,
                    hiddenAppsPicker = Picker()
                )
            }
        }
    }

    private fun onHiddenAppsSave() {
        viewModelScope.launch {
            val hiddenApps = state.value.hiddenAppsPicker.selectedApps

            _state.update {
                it.copy(
                    showHiddenAppsPicker = false,
                    hiddenAppsPicker = Picker()
                )
            }

            appsRepository.setHiddenApps(hiddenApps)
        }
    }

    private fun onSecureAppClick(packageName: String) {
        viewModelScope.launch {
            val selectedApps = state.value.secureAppsPicker.selectedApps.toMutableSet()

            if (packageName in selectedApps) {
                selectedApps.remove(packageName)
            } else {
                selectedApps.add(packageName)
            }

            _state.update {
                it.copy(
                    secureAppsPicker = it.secureAppsPicker.copy(
                        selectedApps = selectedApps
                    )
                )
            }
        }
    }

    private fun onSecureAppsSave() {
        viewModelScope.launch {
            val secureApps = state.value.secureAppsPicker.selectedApps

            _state.update {
                it.copy(
                    showSecureAppsPicker = false,
                    secureAppsPicker = Picker()
                )
            }

            appsRepository.setSecureApps(secureApps)
        }
    }

    private fun onSecureAppsClose() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showSecureAppsPicker = false,
                    secureAppsPicker = Picker()
                )
            }
        }
    }
}