package org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen.QuickPanelSettingsScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen.QuickPanelSettingsScreenState as ScreenState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen.QuickPanelSettingsScreenUICallback as UICallback

class QuickPanelSettingsScreenVM(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    private val _uiCallback = Channel<UICallback>()
    val uiCallback = _uiCallback.receiveAsFlow()

    init {
        settingsRepository.appSettings.onEach { settings ->
            _state.update {
                it.copy(
                    loading = false,
                    sections = settings.quickPanel.sections,
                    widgets = settings.quickPanel.widgets
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: ScreenIntent) {
        when (intent) {
            ScreenIntent.Back -> onBack()
            is ScreenIntent.SectionReorder -> onSectionReorder(intent.from, intent.to)
            ScreenIntent.SectionDragStop -> onDragStop()
            is ScreenIntent.HideSectionClick -> onHideSectionClick(intent.id, intent.hidden)
            is ScreenIntent.WidgetReorder -> onWidgetReorder(intent.from, intent.to)
            ScreenIntent.WidgetDragStop -> onWidgetDragStop()
        }
    }

    private fun onBack() {
        viewModelScope.launch { _uiCallback.send(UICallback.Back) }
    }

    private fun onSectionReorder(from: Int, to: Int) {
        _state.update {
            it.copy(
                sections = it.sections.toMutableList().apply {
                    add(to, removeAt(from))
                }
            )
        }
    }

    private fun onDragStop() {
        viewModelScope.launch {
            val sections = state.value.sections
            settingsRepository.setQuickSections(sections)
        }
    }

    private fun onHideSectionClick(id: String, hidden: Boolean) {
        viewModelScope.launch {
            val index = state.value.sections.indexOfFirst { it.id == id }
            val newSection = state.value.sections[index].copy(hidden = hidden)

            val newSections = state.value.sections.toMutableList().apply {
                set(index, newSection)
            }

            _state.update { it.copy(sections = newSections) }

            settingsRepository.setQuickSections(newSections)
        }
    }

    private fun onWidgetReorder(from: Int, to: Int) {
        _state.update {
            it.copy(
                widgets = it.widgets.toMutableList().apply {
                    add(to, removeAt(from))
                }
            )
        }
    }

    private fun onWidgetDragStop() {
        viewModelScope.launch {
            val widgets = state.value.widgets
            settingsRepository.setWidgets(widgets)
        }
    }
}