package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen

sealed interface HomeScreenIntent {
    data object ClockClick: HomeScreenIntent
    data object DateClick: HomeScreenIntent
    data object AppsButtonClick : HomeScreenIntent
    data object SearchButtonClick : HomeScreenIntent
    data object QuickPanelButtonClick : HomeScreenIntent
    data object LongClick : HomeScreenIntent
    data object MenuDismiss : HomeScreenIntent
    data object SetWallpaperClick : HomeScreenIntent
    data object SettingsClick : HomeScreenIntent
    data object SwipeUp : HomeScreenIntent
    data object SwipeDown : HomeScreenIntent
}

sealed interface HomeScreenUICallback{
    data object GoToQuickPanel: HomeScreenUICallback
    data object GoToApps: HomeScreenUICallback
    data object ExpandSheet: HomeScreenUICallback
}