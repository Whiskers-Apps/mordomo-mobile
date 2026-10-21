package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetProviderInfo
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDo
import org.whiskersapps.mordomo.mobile.core.features.widgets.Widget

data class QuickPanelScreenState(
    val loading: Boolean = false,
    val toDos: List<ToDo> = emptyList(),
    val quickApps: List<App> = emptyList(),
    val addToDoDialog: AddToDoDialogState = AddToDoDialogState(),
    val widgets: List<Widget> = emptyList(),
    val widgetPickerState: WidgetPickerState = WidgetPickerState(),
    val editWidgets: Boolean = false,
) {
    data class AddToDoDialogState(
        val show: Boolean = false,
        val content: String = "",
        val contentError: String? = null
    )

    data class WidgetPickerState(
        val show: Boolean = false,
        val apps: List<App> = emptyList(),
        val searchText: String = "",
        val openedWidgets: Set<String> = emptySet()
    )
}
