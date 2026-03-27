package org.whiskersapps.mordomo.mobile.ui.main.settings.search_engines_screen

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEngine
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEnginesRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.core.utils.isFullURL
import org.whiskersapps.mordomo.mobile.ui.main.settings.search_engines_screen.SearchEnginesScreenState.AddEngineDialogState
import org.whiskersapps.mordomo.mobile.ui.main.settings.search_engines_screen.SearchEnginesScreenState.EditEngineDialogState
import org.whiskersapps.mordomo.mobile.ui.main.settings.search_engines_screen.SearchEnginesScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.main.settings.search_engines_screen.SearchEnginesScreenState as ScreenState

class SearchEnginesScreenVM(
    val searchEnginesRepository: SearchEnginesRepository,
    val settingsRepository: SettingsRepository
) : ViewModel() {
    private val mainIntent = MainIntent()
    private val addDialogIntent = AddDialogIntent()
    private val editDialogIntent = EditDialogIntent()

    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    private var pendingSearchEngine: SearchEngine? = null
    private var preDeleteSearchEngines: List<SearchEngine> = emptyList()

    init {
        combine(
            searchEnginesRepository.searchEngines,
            settingsRepository.appSettings
        ) { searchEngines, appSettings ->
            _state.update {
                it.copy(
                    loading = false,
                    searchEngines = searchEngines,
                    defaultSearchEngineId = appSettings.search.defaultSearchEngine
                )
            }

            println("Default Id: ${appSettings.search.defaultSearchEngine}")
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: ScreenIntent) {
        when (intent) {
            ScreenIntent.Back -> mainIntent.onBack()
            ScreenIntent.FABClick -> mainIntent.onFABClick()
            is ScreenIntent.SearchEngineClick -> mainIntent.onSearchEngineClick(intent.engine)
            is ScreenIntent.EditSnackResult -> mainIntent.onEditSnackResult(intent.result)

            is AddEngineDialogIntent -> when (intent) {
                is AddEngineDialogIntent.NameType -> addDialogIntent.onNameType(intent.text)
                is AddEngineDialogIntent.QueryType -> addDialogIntent.onQueryType(intent.text)
                AddEngineDialogIntent.SaveClick -> addDialogIntent.onSaveClick()
                AddEngineDialogIntent.Close -> addDialogIntent.onClose()
            }

            is EditEngineDialogIntent -> when (intent) {
                is EditEngineDialogIntent.NameType -> editDialogIntent.onNameType(intent.text)
                is EditEngineDialogIntent.QueryType -> editDialogIntent.onQueryType(intent.text)
                is EditEngineDialogIntent.DefaultToggle -> editDialogIntent.onDefaultToggle(intent.value)
                EditEngineDialogIntent.DeleteSlide -> editDialogIntent.onDeleteSlide()
                EditEngineDialogIntent.SaveClick -> editDialogIntent.onSaveClick()
                EditEngineDialogIntent.Close -> editDialogIntent.onClose()
            }
        }
    }

    private inner class MainIntent {
        fun onBack() {
            viewModelScope.launch {
                pendingSearchEngine?.let { searchEngine ->
                    searchEnginesRepository.deleteSearchEngine(searchEngine)
                }
            }
        }

        fun onFABClick() {
            _state.update { it.copy(addEngineDialogState = AddEngineDialogState(show = true)) }
        }

        fun onSearchEngineClick(engine: SearchEngine) {
            viewModelScope.launch {
                pendingSearchEngine = engine

                _state.update {
                    it.copy(
                        editEngineDialogState = EditEngineDialogState(
                            show = true,
                            name = engine.name,
                            query = engine.query,
                            default = settingsRepository.appSettings.value.search.defaultSearchEngine == engine.id,
                            saveEnabled = true
                        )
                    )
                }
            }
        }

        fun onEditSnackResult(result: SnackbarResult) {
            viewModelScope.launch {
                when (result) {
                    SnackbarResult.Dismissed -> {
                        searchEnginesRepository.deleteSearchEngine(pendingSearchEngine!!)
                        pendingSearchEngine = null
                    }

                    SnackbarResult.ActionPerformed -> {
                        _state.update { it.copy(searchEngines = preDeleteSearchEngines) }
                        pendingSearchEngine = null
                    }
                }

                _state.update {
                    it.copy(
                        showUndoSnack = false,
                        editEngineDialogState = EditEngineDialogState()
                    )
                }
            }
        }
    }

    private inner class AddDialogIntent {
        fun onNameType(text: String) {
            _state.update { it.copy(addEngineDialogState = it.addEngineDialogState.copy(name = text)) }

            verifyFields()
        }

        fun onQueryType(text: String) {
            _state.update { it.copy(addEngineDialogState = it.addEngineDialogState.copy(query = text)) }

            verifyFields()
        }

        fun onSaveClick() {
            viewModelScope.launch {
                val engine = SearchEngine(
                    name = state.value.addEngineDialogState.name,
                    query = state.value.addEngineDialogState.query
                )

                closeDialog()

                searchEnginesRepository.addSearchEngine(engine)
            }
        }

        fun onClose() = closeDialog()

        private fun verifyFields() {
            val name = state.value.addEngineDialogState.name
            val query = state.value.addEngineDialogState.query
            val enabled = name.isNotBlank() && query.isFullURL() && query.contains("%s")

            _state.update {
                it.copy(addEngineDialogState = it.addEngineDialogState.copy(saveEnabled = enabled))
            }
        }

        private fun closeDialog() {
            _state.update { it.copy(addEngineDialogState = AddEngineDialogState()) }
        }
    }

    private inner class EditDialogIntent {
        fun onNameType(text: String) {
            _state.update { it.copy(editEngineDialogState = it.editEngineDialogState.copy(name = text)) }

            verifyFields()
        }

        fun onQueryType(text: String) {
            _state.update { it.copy(editEngineDialogState = it.editEngineDialogState.copy(query = text)) }

            verifyFields()
        }

        fun onDefaultToggle(value: Boolean) {
            _state.update { it.copy(editEngineDialogState = it.editEngineDialogState.copy(default = value)) }
        }

        fun onDeleteSlide() {
            viewModelScope.launch {
                preDeleteSearchEngines = state.value.searchEngines

                _state.update {
                    it.copy(
                        showUndoSnack = true,
                        searchEngines = it.searchEngines.filter { se -> se.id != pendingSearchEngine!!.id },
                        editEngineDialogState = it.editEngineDialogState.copy(show = false)
                    )
                }
            }
        }

        fun onSaveClick() {
            viewModelScope.launch {
                val searchEngine = pendingSearchEngine!!.copy(
                    name = state.value.editEngineDialogState.name,
                    query = state.value.editEngineDialogState.query
                )

                val defaultEngineId =
                    settingsRepository.appSettings.value.search.defaultSearchEngine

                val defaultToggled = state.value.editEngineDialogState.default

                val pendingEngineId = pendingSearchEngine!!.id

                if (defaultToggled && defaultEngineId != pendingEngineId) {
                    settingsRepository.setDefaultEngineId(pendingEngineId)
                }

                if (!defaultToggled && defaultEngineId == pendingEngineId) {
                    settingsRepository.setDefaultEngineId(null)
                }

                closeDialog()

                searchEnginesRepository.updateSearchEngine(searchEngine)
            }
        }

        fun onClose() = closeDialog()

        private fun verifyFields() {
            val name = state.value.editEngineDialogState.name
            val query = state.value.editEngineDialogState.query
            val enabled = name.isNotBlank() && query.isFullURL() && query.contains("%s")

            _state.update {
                it.copy(editEngineDialogState = it.editEngineDialogState.copy(saveEnabled = enabled))
            }
        }

        fun closeDialog() {
            _state.update { it.copy(editEngineDialogState = EditEngineDialogState()) }
            pendingSearchEngine = null
        }
    }
}