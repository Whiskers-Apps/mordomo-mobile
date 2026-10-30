package org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen

import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings.QuickPanel.QuickSection
import org.whiskersapps.mordomo.mobile.core.features.widgets.WidgetSetting

data class QuickPanelSettingsScreenState(
    val loading: Boolean = true,
    val sections: List<QuickSection> = emptyList(),
    val widgets: List<WidgetSetting> = emptyList()
)
