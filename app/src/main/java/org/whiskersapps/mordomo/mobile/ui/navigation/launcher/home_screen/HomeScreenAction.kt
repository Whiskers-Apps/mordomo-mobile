package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen

sealed interface HomeScreenAction {
    data object AppsButtonClick : HomeScreenAction
    data object SearchButtonClick : HomeScreenAction
}