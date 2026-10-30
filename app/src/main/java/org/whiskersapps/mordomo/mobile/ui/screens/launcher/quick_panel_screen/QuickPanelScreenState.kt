package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen

import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.QuickPanel.QuickSection
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDo
import org.whiskersapps.mordomo.mobile.core.features.widgets.Widget
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables.AppsPickerState

data class QuickPanelScreenState(
    val loading: Boolean = false,
    val sections: List<QuickSection> = emptyList(),
    val toDos: List<ToDo> = emptyList(),
    val quickApps: List<App> = emptyList(),
    val quickBookmarks: List<App> = emptyList(),
    val addToDoDialog: AddToDoDialogState = AddToDoDialogState(),
    val widgets: List<Widget> = emptyList(),
    val widgetPickerState: WidgetPickerState = WidgetPickerState(),
    val appsPickerState: QuickAppsPickerState = QuickAppsPickerState(),
    val bookmarksPickerState: QuickBookmarksPickerState = QuickBookmarksPickerState(),
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

    data class QuickAppsPickerState(
        val show: Boolean = false,
        val pickerState: AppsPickerState = AppsPickerState()
    )

    data class QuickBookmarksPickerState(
        val show: Boolean = false,
        val bookmarks: List<Bookmark> = emptyList(),
        val selectedBookmarks: Set<Int> = emptySet()
    )
}
