package org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen

sealed interface LockSettingsScreenIntent {
    data object Back : LockSettingsScreenIntent
    data class MethodSelect(val method: String) : LockSettingsScreenIntent
    data object TestLockClick : LockSettingsScreenIntent
}

sealed interface LockSettingsScreenUICallback {
    data object GoBack : LockSettingsScreenUICallback
}