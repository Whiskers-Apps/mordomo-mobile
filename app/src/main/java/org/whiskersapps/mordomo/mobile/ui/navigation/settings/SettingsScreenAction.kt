package org.whiskersapps.mordomo.mobile.ui.navigation.settings

sealed interface SettingsScreenAction {
    data object SetDefaultLauncherClick : SettingsScreenAction
    data object StyleSectionClick: SettingsScreenAction
    data object AppsSectionClick: SettingsScreenAction
    data object HomeSectionClick: SettingsScreenAction
    data object BookmarksSectionClick: SettingsScreenAction
    data object SearchEnginesSectionClick: SettingsScreenAction
    data object SecuritySectionClick: SettingsScreenAction
    data object LockSectionClick: SettingsScreenAction
    data object AboutSectionClick: SettingsScreenAction
}