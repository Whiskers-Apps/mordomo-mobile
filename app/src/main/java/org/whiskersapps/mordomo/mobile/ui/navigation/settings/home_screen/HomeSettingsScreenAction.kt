package org.whiskersapps.mordomo.mobile.ui.navigation.settings.home_screen

sealed interface HomeSettingsScreenAction {
    data object BackClick : HomeSettingsScreenAction
    data class ClockStyleSet(val style: String) : HomeSettingsScreenAction

    data class ShowQuickButtonClick(val value: Boolean): HomeSettingsScreenAction

    data class ShowSearchButtonClick(val value: Boolean): HomeSettingsScreenAction

    data class ShowAppsButtonClick(val value: Boolean): HomeSettingsScreenAction

    // Date Format Dialog
    data object DateFormatClick : HomeSettingsScreenAction
    data class DateFormatType(val format: String) : HomeSettingsScreenAction
    data object DateFormatDialogCancel : HomeSettingsScreenAction
    data class DateFormatDialogSave(val value: String): HomeSettingsScreenAction


}