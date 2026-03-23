package org.whiskersapps.mordomo.mobile.ui.main.settings.home_screen

sealed interface HomeSettingsScreenIntent {
    data object BackClick : HomeSettingsScreenIntent
    data class ClockStyleSet(val style: String) : HomeSettingsScreenIntent

    data class ClockPositionSet(val position: String) : HomeSettingsScreenIntent

    data class ShowDateClick(val value: Boolean) : HomeSettingsScreenIntent

    data class ShowQuickButtonClick(val value: Boolean) : HomeSettingsScreenIntent

    data class ShowSearchButtonClick(val value: Boolean) : HomeSettingsScreenIntent

    data class ShowAppsButtonClick(val value: Boolean) : HomeSettingsScreenIntent

    // Date Format Dialog
    data object DateFormatClick : HomeSettingsScreenIntent
    data class DateFormatType(val format: String) : HomeSettingsScreenIntent
    data object DateFormatDialogCancel : HomeSettingsScreenIntent
    data class DateFormatDialogSave(val value: String) : HomeSettingsScreenIntent


}