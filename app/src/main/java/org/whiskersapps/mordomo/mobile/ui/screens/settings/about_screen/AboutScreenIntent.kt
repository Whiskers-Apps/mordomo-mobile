package org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen

sealed interface AboutScreenIntent {
    data object BackClick : AboutScreenIntent
    data object URLClick : AboutScreenIntent
}

sealed interface AboutScreenUICallback {
    data object GoBack : AboutScreenUICallback
}