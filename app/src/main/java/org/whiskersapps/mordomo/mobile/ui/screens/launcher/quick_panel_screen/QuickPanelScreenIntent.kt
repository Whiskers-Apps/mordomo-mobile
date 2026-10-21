package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen

import org.whiskersapps.mordomo.mobile.core.features.widgets.Widget

sealed interface QuickPanelScreenIntent {
    data object AddToDoClick : QuickPanelScreenIntent
    data class ToDoClick(val id: Int) : QuickPanelScreenIntent
    data class ToDoDeleteClick(val id: Int) : QuickPanelScreenIntent
    data object WidgetPickerClose : QuickPanelScreenIntent
    data object AddWidgetClick : QuickPanelScreenIntent
    data class AppWidgetCardClick(val name: String) : QuickPanelScreenIntent
    data object WidgetClick : QuickPanelScreenIntent
    data class WidgetBound(val id: Int, val name: String) : QuickPanelScreenIntent
    data class DeleteWidgetClick(val widget: Widget) : QuickPanelScreenIntent
    data class SaveWidgetClick(val id: Int, val rows: Int, val cols: Int) : QuickPanelScreenIntent
    data class SearchWidgetType(val text: String) : QuickPanelScreenIntent
}

sealed interface AddToDoDialogIntent : QuickPanelScreenIntent {
    data object Close : AddToDoDialogIntent
    data class ContentType(val text: String) : AddToDoDialogIntent
    data object AddClick : AddToDoDialogIntent
}