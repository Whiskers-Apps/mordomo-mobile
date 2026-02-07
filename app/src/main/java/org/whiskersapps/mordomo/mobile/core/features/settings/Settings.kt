package org.whiskersapps.mordomo.mobile.core.features.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class Settings(
    val setup: Setup = Setup(),
    val home: Home = Home()
) {
    companion object {
        val KEY_SETUP_COMPLETED = booleanPreferencesKey("setup_completed")
        const val DEFAULT_SETUP_COMPLETED = false

        val KEY_CLOCK_STYLE = stringPreferencesKey("clock_style")
        const val DEFAULT_CLOCK_STYLE = SettingsOptions.ClockStyle.HIDDEN

        val KEY_DATE_FORMAT = stringPreferencesKey("date_format")
        const val DEFAULT_DATE_FORMAT = "EEEE - dd/MM/yyyy"
    }

    data class Setup(
        val completed: Boolean = DEFAULT_SETUP_COMPLETED
    )

    data class Home(
        val clockStyle: String = DEFAULT_CLOCK_STYLE,
        val dateFormat: String = DEFAULT_DATE_FORMAT
    )

}


object SettingsOptions {
    object ClockStyle {
        const val MINIMAL = "minimal"
        const val ISLAND = "island"
        const val HIDDEN = "hidden"
    }
}


