package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen

sealed interface HomeScreenAction {
    data object AppsButtonClick : HomeScreenAction
    data object SearchButtonClick : HomeScreenAction
    data object QuickPanelButtonClick : HomeScreenAction
    data object LongClick : HomeScreenAction
    data object MenuDismiss : HomeScreenAction
    data object SetWallpaperClick : HomeScreenAction
    data object SettingsClick : HomeScreenAction
}