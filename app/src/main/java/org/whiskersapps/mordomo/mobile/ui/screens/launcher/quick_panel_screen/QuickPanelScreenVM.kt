package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen

import android.appwidget.AppWidgetProviderInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiskersapps.lib.Sniffer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDo
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDoRepository
import org.whiskersapps.mordomo.mobile.core.features.widgets.Widget
import org.whiskersapps.mordomo.mobile.core.features.widgets.WidgetSetting
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState.AddToDoDialogState
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState.WidgetPickerState
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenIntent as Intent

class QuickPanelScreenVM(
    private val settingsRepository: SettingsRepository,
    val todoRepository: ToDoRepository,
    private val appsRepository: AppsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(QuickPanelScreenState())
    val state = _state.asStateFlow()

    private var appsWithWidgets = listOf<App>()

    private val sniffer = Sniffer()

    init {
        combine(
            settingsRepository.appSettings,
            todoRepository.toDos,
        ) { settings, toDos ->
            val allWidgets = getAllWidgets()
            val widgetsSettings = settings.home.widgets

            appsWithWidgets = appsRepository.apps.filter { app -> app.widgets.isNotEmpty() }

            val widgets = widgetsSettings.mapNotNull { setting ->
                val info = allWidgets.firstOrNull { it.provider.className == setting.name }
                    ?: return@mapNotNull null

                Widget(
                    id = setting.id,
                    name = setting.name,
                    info = info,
                    rows = setting.rows,
                    cols = setting.cols
                )
            }

            _state.update {
                it.copy(
                    toDos = toDos,
                    widgets = widgets,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(intent: Intent) {
        when (intent) {
            Intent.AddToDoClick -> onAddToDoClick()
            is Intent.ToDoClick -> onToDoClick(intent.id)
            is Intent.ToDoDeleteClick -> onToDoDelete(intent.id)

            is AddToDoDialogIntent -> when (intent) {
                AddToDoDialogIntent.AddClick -> onDialogAddToDoClick()
                AddToDoDialogIntent.Close -> onDialogClose()
                is AddToDoDialogIntent.ContentType -> onContentType(intent.text)
            }

            Intent.AddWidgetClick -> onAddWidgetClick()
            Intent.WidgetPickerClose -> onWidgetPickerClose()
            is Intent.WidgetClick -> onWidgetClick()
            is Intent.WidgetBound -> onWidgetBound(intent.id, intent.name)
            is Intent.DeleteWidgetClick -> onDeleteWidgetClick(intent.widget)
            is Intent.SaveWidgetClick -> onSaveWidgetClick(intent.id, intent.rows, intent.cols)
            is Intent.SearchWidgetType -> onSearchWidgetType(intent.text)
        }
    }


    private fun onAddToDoClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(addToDoDialog = AddToDoDialogState(show = true))
            }
        }
    }

    private fun onDialogClose() {
        viewModelScope.launch {
            _state.update { it.copy(addToDoDialog = AddToDoDialogState()) }
        }
    }

    private fun onContentType(text: String) {
        _state.update { it.copy(addToDoDialog = it.addToDoDialog.copy(content = text)) }
    }

    private fun onDialogAddToDoClick() {
        viewModelScope.launch {
            val toDo = ToDo(content = state.value.addToDoDialog.content)

            _state.update { it.copy(addToDoDialog = AddToDoDialogState()) }

            todoRepository.addToDo(toDo)
        }
    }

    private fun onToDoClick(id: Int) {
        viewModelScope.launch {
            todoRepository.toggleToDo(id)
        }
    }

    private fun onToDoDelete(id: Int) {
        viewModelScope.launch { todoRepository.deleteToDo(id) }
    }

    private fun onAddWidgetClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    widgetPickerState = WidgetPickerState(
                        show = true,
                        apps = appsWithWidgets
                    )
                )
            }
        }
    }

    private fun onWidgetPickerClose() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    widgetPickerState = WidgetPickerState(),
                )
            }
        }
    }

    private fun onWidgetClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    widgetPickerState = WidgetPickerState(),
                )
            }
        }
    }

    private fun onWidgetBound(id: Int, name: String) {
        viewModelScope.launch {
            val widgets = settingsRepository.appSettings.value.home.widgets

            val newWidgets = widgets.toMutableList().apply {
                add(
                    WidgetSetting(
                        index = widgets.maxByOrNull { it.index }?.index?.plus(1) ?: 1,
                        id = id,
                        name = name,
                        rows = 2,
                        cols = 4
                    )
                )
            }

            settingsRepository.setWidgets(newWidgets)
        }
    }

    private fun onDeleteWidgetClick(widget: Widget) {
        viewModelScope.launch {
            val widgetsSettings = settingsRepository.appSettings.value.home.widgets
            val newWidgetsSettings = widgetsSettings.toMutableList().apply {
                removeIf { it.id == widget.id }
            }

            settingsRepository.setWidgets(newWidgetsSettings)
        }
    }

    private fun onSaveWidgetClick(id: Int, rows: Int, cols: Int) {
        viewModelScope.launch {
            val widgetSettings = settingsRepository.appSettings.value.home.widgets
            val newWidgetsSettings = widgetSettings.toMutableList().map { widget ->
                if (widget.id == id) {
                    widget.copy(rows = rows, cols = cols)
                } else {
                    widget
                }
            }

            settingsRepository.setWidgets(newWidgetsSettings)
        }
    }

    private fun onSearchWidgetType(text: String) {
        _state.update {
            it.copy(
                widgetPickerState = it.widgetPickerState.copy(searchText = text)
            )
        }

        viewModelScope.launch {
            val newApps = if (text.isBlank())
                appsWithWidgets
            else
                appsWithWidgets.filter { sniffer.matches(it.name, text) }

            _state.update { it.copy(widgetPickerState = it.widgetPickerState.copy(apps = newApps)) }
        }
    }

    // -----------------------------------------------------
    private fun getAllWidgets(): List<AppWidgetProviderInfo> {
        val widgets = mutableListOf<AppWidgetProviderInfo>()

        for (app in appsRepository.apps) {
            if (app.widgets.isNotEmpty())
                widgets.addAll(app.widgets)
        }

        return widgets
    }

}