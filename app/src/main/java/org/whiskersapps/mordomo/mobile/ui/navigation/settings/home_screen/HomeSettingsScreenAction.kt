package org.whiskersapps.mordomo.mobile.ui.navigation.settings.home_screen

sealed interface HomeSettingsScreenAction {
    data object BackClick : HomeSettingsScreenAction
    data class ClockStyleSet(val style: String) : HomeSettingsScreenAction

    // Date Format Dialog
    data object DateFormatClick : HomeSettingsScreenAction
    data class DateFormatType(val format: String) : HomeSettingsScreenAction
    data object DateFormatDialogCancel : HomeSettingsScreenAction
    data class DateFormatDialogSave(val value: String): HomeSettingsScreenAction

}