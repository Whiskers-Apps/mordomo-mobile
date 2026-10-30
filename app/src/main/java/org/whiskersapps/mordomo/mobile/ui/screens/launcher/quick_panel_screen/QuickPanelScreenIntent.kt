package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
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
    data object AddAppClick : QuickPanelScreenIntent
    data object AppsPickerClose : QuickPanelScreenIntent
    data class AppsPickerSelect(val packageName: String) : QuickPanelScreenIntent
    data object AppsPickerSave : QuickPanelScreenIntent
    data class AppClick(val app: App) : QuickPanelScreenIntent
    data object AddBookmarkClick : QuickPanelScreenIntent
    data object BookmarksPickerClose : QuickPanelScreenIntent
    data class BookmarksPickerSelect(val id: Int) : QuickPanelScreenIntent
    data object BookmarksPickerSave : QuickPanelScreenIntent
    data class BookmarkClick(val bookmark: Bookmark) : QuickPanelScreenIntent
}

sealed interface AddToDoDialogIntent : QuickPanelScreenIntent {
    data object Close : AddToDoDialogIntent
    data class ContentType(val text: String) : AddToDoDialogIntent
    data object AddClick : AddToDoDialogIntent
}

sealed interface QuickPanelScreenCallback {
    data object GoHome : QuickPanelScreenCallback
}