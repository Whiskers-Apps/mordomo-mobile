package org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen

sealed interface QuickPanelSettingsScreenIntent {
    data object Back : QuickPanelSettingsScreenIntent
    data class SectionReorder(val from: Int, val to: Int) : QuickPanelSettingsScreenIntent
    data object SectionDragStop : QuickPanelSettingsScreenIntent
    data class HideSectionClick(val id: String, val hidden: Boolean) :
        QuickPanelSettingsScreenIntent

    data class WidgetReorder(val from: Int, val to: Int) : QuickPanelSettingsScreenIntent
    data object WidgetDragStop : QuickPanelSettingsScreenIntent
}

sealed interface QuickPanelSettingsScreenUICallback {
    data object Back : QuickPanelSettingsScreenUICallback
}