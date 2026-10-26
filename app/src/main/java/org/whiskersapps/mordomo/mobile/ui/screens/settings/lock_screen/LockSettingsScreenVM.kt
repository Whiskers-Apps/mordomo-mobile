package org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen

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
import org.whiskersapps.mordomo.mobile.core.features.lock.LockRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen.LockSettingsScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen.LockSettingsScreenUICallback as UICallback

class LockSettingsScreenVM(
    private val settingsRepository: SettingsRepository,
    private val lockRepository: LockRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LockSettingsScreenState())
    val state = _state.asStateFlow()

    private val _uiCallback = Channel<UICallback>()
    val uiCallback = _uiCallback.receiveAsFlow()

    init {
        settingsRepository.appSettings.onEach { settings ->
            _state.update {
                it.copy(
                    loading = false,
                    lockMethod = settings.home.lockScreenMethod,
                    warningMessage = when (settings.home.lockScreenMethod) {
                        SettingsValues.LockScreenMethod.ROOT -> "This method requires root access. You will need a superuser manager like Magisk or KernelSu."
                        SettingsValues.LockScreenMethod.ACCESSIBILITY -> "This method requires accessibility access. This is the ONLY thing the app uses it for."
                        else -> null
                    },
                    showAccessibilityCard = settings.home.lockScreenMethod == SettingsValues.LockScreenMethod.ACCESSIBILITY,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: ScreenIntent) {
        when (intent) {
            ScreenIntent.Back -> onBack()
            is ScreenIntent.MethodSelect -> onMethodSelect(intent.method)
            ScreenIntent.TestLockClick -> onTestLockClick()
        }
    }

    private fun onBack() {
        viewModelScope.launch {
            _uiCallback.send(UICallback.GoBack)
        }
    }

    private fun onMethodSelect(method: String) {
        viewModelScope.launch {
            settingsRepository.setLockScreenMethod(method)
        }
    }

    private fun onTestLockClick() {
        viewModelScope.launch {
            lockRepository.lockScreen()
        }
    }
}