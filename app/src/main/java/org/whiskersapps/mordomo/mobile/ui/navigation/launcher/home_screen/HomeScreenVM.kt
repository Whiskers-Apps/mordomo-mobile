package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeScreenVM(
    val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                val calendar = Calendar.getInstance()
                val locale = Locale.getDefault()

                val formatter =
                    SimpleDateFormat(settingsRepository.settings.value.home.dateFormat, locale)

                val hours = String.format(locale, "%02d", calendar.get(Calendar.HOUR_OF_DAY))
                val minutes = String.format(locale, "%02d", calendar.get(Calendar.MINUTE))

                val clockText = "$hours:$minutes"
                val dateText = formatter.format(calendar.time)

                _state.update {
                    it.copy(
                        clock = clockText,
                        date = dateText
                    )
                }

                delay(1000)
            }
        }
    }
}