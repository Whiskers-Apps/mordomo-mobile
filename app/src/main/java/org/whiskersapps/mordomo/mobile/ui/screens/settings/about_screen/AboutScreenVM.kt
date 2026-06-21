package org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.BuildConfig
import android.content.Intent as AndroidIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen.AboutScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen.AboutScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen.AboutScreenUICallback as UICallback

class AboutScreenVM(
    val app: Application
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _uiCallback = Channel<UICallback>()
    val uiCallback = _uiCallback.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                loading = false,
                version = BuildConfig.VERSION_NAME
            )
        }
    }

    fun onIntent(intent: Intent){
        when(intent){
            Intent.BackClick -> onBackClick()
            Intent.URLClick -> onURLClick()
        }
    }

    private fun onBackClick(){
        viewModelScope.launch {
            _uiCallback.send(UICallback.GoBack)
        }
    }

    private fun onURLClick(){
        viewModelScope.launch {
            val intent = AndroidIntent(AndroidIntent.ACTION_VIEW, state.value.repo.toUri())
            intent.flags = AndroidIntent.FLAG_ACTIVITY_NEW_TASK

            app.startActivity(intent)
        }
    }
}