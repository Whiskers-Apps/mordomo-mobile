package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen

import androidx.compose.material3.SnackbarResult
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEngine

sealed interface SearchEnginesScreenIntent {
    data object Back : SearchEnginesScreenIntent
    data object FABClick : SearchEnginesScreenIntent
    data class SearchEngineClick(val engine: SearchEngine) : SearchEnginesScreenIntent
    data class EditSnackResult(val result: SnackbarResult) : SearchEnginesScreenIntent
}

sealed interface AddEngineDialogIntent : SearchEnginesScreenIntent {
    data class NameType(val text: String) : AddEngineDialogIntent
    data class QueryType(val text: String) : AddEngineDialogIntent
    data object SaveClick : AddEngineDialogIntent
    data object Close : AddEngineDialogIntent
}

sealed interface EditEngineDialogIntent : SearchEnginesScreenIntent {
    data class NameType(val text: String) : EditEngineDialogIntent
    data class QueryType(val text: String) : EditEngineDialogIntent
    data class DefaultToggle(val value: Boolean) : EditEngineDialogIntent
    data object DeleteClick : EditEngineDialogIntent
    data object SaveClick : EditEngineDialogIntent
    data object Close : EditEngineDialogIntent
}