package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenState.HiddenAppsPicker
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
                    hiddenAppsPicker = HiddenAppsPicker(
                        apps = appsRepository.apps,
                        selectedApps = appsRepository.getHiddenApps()
                    )
                )
            }
        }
    }

    private fun onSecureAppsClick() {

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
                    hiddenAppsPicker = HiddenAppsPicker()
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
                    hiddenAppsPicker = HiddenAppsPicker()
                )
            }

            appsRepository.setHiddenApps(hiddenApps)
        }
    }
}