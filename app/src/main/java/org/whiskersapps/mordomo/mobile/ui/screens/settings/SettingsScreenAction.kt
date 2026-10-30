package org.whiskersapps.mordomo.mobile.ui.screens.settings

sealed interface SettingsScreenIntent {
    data object SetDefaultLauncherClick : SettingsScreenIntent
    data object StyleSectionClick : SettingsScreenIntent
    data object QuickPanelSectionClick : SettingsScreenIntent
    data object AppsSectionClick : SettingsScreenIntent
    data object HomeSectionClick : SettingsScreenIntent
    data object SearchSectionClick : SettingsScreenIntent
    data object BookmarksSectionClick : SettingsScreenIntent
    data object SearchEnginesSectionClick : SettingsScreenIntent
    data object SecuritySectionClick : SettingsScreenIntent
    data object LockSectionClick : SettingsScreenIntent
    data object AboutSectionClick : SettingsScreenIntent
}