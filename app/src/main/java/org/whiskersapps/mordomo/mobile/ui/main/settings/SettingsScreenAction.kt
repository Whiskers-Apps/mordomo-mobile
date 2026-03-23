package org.whiskersapps.mordomo.mobile.ui.main.settings

sealed interface SettingsScreenIntent {
    data object SetDefaultLauncherClick : SettingsScreenIntent
    data object StyleSectionClick : SettingsScreenIntent
    data object AppsSectionClick : SettingsScreenIntent
    data object HomeSectionClick : SettingsScreenIntent
    data object BookmarksSectionClick : SettingsScreenIntent
    data object SearchEnginesSectionClick : SettingsScreenIntent
    data object SecuritySectionClick : SettingsScreenIntent
    data object LockSectionClick : SettingsScreenIntent
    data object AboutSectionClick : SettingsScreenIntent
}