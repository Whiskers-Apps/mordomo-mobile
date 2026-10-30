package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen

import android.app.Application
import android.appwidget.AppWidgetProviderInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiskersapps.lib.Sniffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.BookmarksRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDo
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDoRepository
import org.whiskersapps.mordomo.mobile.core.features.widgets.Widget
import org.whiskersapps.mordomo.mobile.core.features.widgets.WidgetSetting
import org.whiskersapps.mordomo.mobile.core.utils.openURL
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState.AddToDoDialogState
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState.QuickAppsPickerState
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState.QuickBookmarksPickerState
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState.WidgetPickerState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables.AppsPickerState
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenCallback as ScreenCallback
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState as ScreenState

class QuickPanelScreenVM(
    private val settingsRepository: SettingsRepository,
    val todoRepository: ToDoRepository,
    private val appsRepository: AppsRepository,
    private val bookmarksRepository: BookmarksRepository,
    private val context: Application
) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    private val _screenCallback = Channel<ScreenCallback>()
    val screenCallback = _screenCallback.receiveAsFlow()

    private var appsWithWidgets = listOf<App>()

    private val sniffer = Sniffer()

    init {
        combine(
            settingsRepository.appSettings,
            todoRepository.toDos,
            appsRepository.filteredApps,
        ) { settings, toDos, apps ->
            val allWidgets = getAllWidgets()
            val widgetsSettings = settings.quickPanel.widgets

            appsWithWidgets = apps.filter { app -> app.widgets.isNotEmpty() }

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

            val quickApps = apps.filter { settings.quickPanel.quickApps.contains(it.packageName) }

            val quickBookmarks = bookmarksRepository.bookmarks.value.filter {
                settings.quickPanel.quickBookmarks.contains(it.id)
            }

            _state.update {
                it.copy(
                    toDos = toDos,
                    widgets = widgets,
                    quickApps = quickApps,
                    quickBookmarks = quickBookmarks,
                    sections = settings.quickPanel.sections
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
            is Intent.AppWidgetCardClick -> onWidgetCardClick(intent.name)
            is Intent.WidgetClick -> onWidgetClick()
            is Intent.WidgetBound -> onWidgetBound(intent.id, intent.name)
            is Intent.DeleteWidgetClick -> onDeleteWidgetClick(intent.widget)
            is Intent.SaveWidgetClick -> onSaveWidgetClick(intent.id, intent.rows, intent.cols)
            is Intent.SearchWidgetType -> onSearchWidgetType(intent.text)
            Intent.AddAppClick -> onAddAppClick()
            Intent.AppsPickerClose -> onAppsPickerClose()
            is Intent.AppsPickerSelect -> onAppsPickerSelect(intent.packageName)
            Intent.AppsPickerSave -> onAppsPickerSave()
            is Intent.AppClick -> onAppClick(intent.app)
            Intent.AddBookmarkClick -> onAddBookmarkClick()
            Intent.BookmarksPickerClose -> onBookmarksPickerClose()
            is Intent.BookmarksPickerSelect -> onBookmarksPickerSelect(intent.id)
            Intent.BookmarksPickerSave -> onBookmarksPickerSave()
            is Intent.BookmarkClick -> onBookmarkClick(intent.bookmark)
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
        _state.update {
            it.copy(
                addToDoDialog = it.addToDoDialog.copy(
                    content = text,
                    contentError = null
                )
            )
        }
    }

    private fun onDialogAddToDoClick() {
        viewModelScope.launch {
            val content = state.value.addToDoDialog.content
            val contentError = if (content.isNotBlank()) null else "TODO must not be empty"

            if (contentError != null) {
                _state.update { it.copy(addToDoDialog = it.addToDoDialog.copy(contentError = contentError)) }
                return@launch
            }

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

    private fun onWidgetCardClick(name: String) {
        viewModelScope.launch {
            val openedWidgets = state.value.widgetPickerState.openedWidgets.toMutableSet()

            if (openedWidgets.contains(name)) {
                openedWidgets.remove(name)
            } else {
                openedWidgets.add(name)
            }

            _state.update { it.copy(widgetPickerState = it.widgetPickerState.copy(openedWidgets = openedWidgets)) }
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
            val widgets = settingsRepository.appSettings.value.quickPanel.widgets

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
            val widgetsSettings = settingsRepository.appSettings.value.quickPanel.widgets
            val newWidgetsSettings = widgetsSettings.toMutableList().apply {
                removeIf { it.id == widget.id }
            }

            settingsRepository.setWidgets(newWidgetsSettings)
        }
    }

    private fun onSaveWidgetClick(id: Int, rows: Int, cols: Int) {
        viewModelScope.launch {
            val widgetSettings = settingsRepository.appSettings.value.quickPanel.widgets
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

    private fun onAddAppClick() {
        viewModelScope.launch {
            val selectedApps = appsRepository.filteredApps.value.filter {
                settingsRepository.appSettings.value.quickPanel.quickApps.contains(it.packageName)
            }.map { it.packageName }

            _state.update {
                it.copy(
                    appsPickerState = QuickAppsPickerState(
                        show = true,
                        pickerState = AppsPickerState(
                            apps = appsRepository.filteredApps.value,
                            selectedApps = selectedApps
                        )
                    )
                )
            }
        }
    }

    private fun onAddBookmarkClick() {
        viewModelScope.launch {
            val selectedBookmarks = bookmarksRepository.bookmarks.value.filter {
                settingsRepository.appSettings.value.quickPanel.quickBookmarks.contains(it.id)
            }.map { it.id }

            _state.update {
                it.copy(
                    bookmarksPickerState = QuickBookmarksPickerState(
                        show = true,
                        bookmarks = bookmarksRepository.bookmarks.value,
                        selectedBookmarks = selectedBookmarks
                    )
                )
            }
        }
    }

    private fun onAppsPickerClose() {
        viewModelScope.launch {
            _state.update { it.copy(appsPickerState = QuickAppsPickerState()) }
        }
    }

    private fun onAppsPickerSelect(packageName: String) {
        viewModelScope.launch {
            val newSelectedApps =
                state.value.appsPickerState.pickerState.selectedApps.toMutableList()

            if (newSelectedApps.contains(packageName))
                newSelectedApps.remove(packageName)
            else
                newSelectedApps.add(packageName)

            _state.update {
                it.copy(
                    appsPickerState = it.appsPickerState.copy(
                        pickerState = it.appsPickerState.pickerState.copy(
                            selectedApps = newSelectedApps
                        )
                    )
                )
            }
        }
    }

    private fun onAppsPickerSave() {
        viewModelScope.launch {
            val apps = state.value.appsPickerState.pickerState.selectedApps

            _state.update { it.copy(appsPickerState = QuickAppsPickerState()) }

            settingsRepository.setQuickApps(apps)
        }
    }

    private fun onAppClick(app: App) {
        viewModelScope.launch {
            appsRepository.openApp(app)

            _screenCallback.send(ScreenCallback.GoHome)
        }
    }

    private fun onBookmarksPickerClose() {
        viewModelScope.launch {
            _state.update { it.copy(bookmarksPickerState = QuickBookmarksPickerState()) }
        }
    }

    private fun onBookmarksPickerSelect(id: Int) {
        viewModelScope.launch {
            val newSelectedBookmarks =
                state.value.bookmarksPickerState.selectedBookmarks.toMutableList()

            if (newSelectedBookmarks.contains(id))
                newSelectedBookmarks.remove(id)
            else
                newSelectedBookmarks.add(id)

            _state.update {
                it.copy(
                    bookmarksPickerState = it.bookmarksPickerState.copy(
                        selectedBookmarks = newSelectedBookmarks
                    )
                )
            }
        }
    }

    private fun onBookmarksPickerSave() {
        viewModelScope.launch {
            val bookmarks = state.value.bookmarksPickerState.selectedBookmarks

            _state.update { it.copy(bookmarksPickerState = QuickBookmarksPickerState()) }

            settingsRepository.setQuickBookmarks(bookmarks)
        }
    }

    private fun onBookmarkClick(bookmark: Bookmark) {
        viewModelScope.launch {
            launch {
                openURL(bookmark.url, context)
            }

            _screenCallback.send(ScreenCallback.GoHome)
        }
    }

    // -----------------------------------------------------
    private fun getAllWidgets(): List<AppWidgetProviderInfo> {
        val widgets = mutableListOf<AppWidgetProviderInfo>()

        for (app in appsRepository.filteredApps.value) {
            if (app.widgets.isNotEmpty())
                widgets.addAll(app.widgets)
        }

        return widgets
    }

}