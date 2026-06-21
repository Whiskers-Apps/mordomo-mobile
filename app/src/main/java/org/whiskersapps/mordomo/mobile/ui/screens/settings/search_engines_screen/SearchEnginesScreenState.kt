package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen

import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEngine

data class SearchEnginesScreenState(
    val loading: Boolean = true,
    val searchEngines: List<SearchEngine> = emptyList(),
    val defaultSearchEngineId: Int? = null,
    val showUndoSnack: Boolean = false,
    val addEngineDialogState: AddEngineDialogState = AddEngineDialogState(),
    val editEngineDialogState: EditEngineDialogState = EditEngineDialogState()
) {
    data class AddEngineDialogState(
        val show: Boolean = false,
        val name: String = "",
        val query: String = "",
        val saveEnabled: Boolean = false
    )

    data class EditEngineDialogState(
        val show: Boolean = false,
        val name: String = "",
        val query: String = "",
        val default: Boolean = false,
        val saveEnabled: Boolean = false
    )
}
