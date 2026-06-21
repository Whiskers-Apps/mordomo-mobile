package org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen

data class AboutScreenState(
    val loading: Boolean = true,
    val version: String = "",
    val repo: String = "https://github.com/Whiskers-Apps/mordomo-mobile"
)
