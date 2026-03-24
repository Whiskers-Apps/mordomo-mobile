package org.whiskersapps.mordomo.mobile.core.features.search_engines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.db.AppDB

class SearchEnginesRepository(
    appDB: AppDB
) {
    private val dao = appDB.searchEnginesDao()

    private val _searchEngines = MutableStateFlow<List<SearchEngine>>(emptyList())
    val searchEngines = _searchEngines.asStateFlow()

    init {
        dao.getAll()
            .onEach { searchEngines -> _searchEngines.update { searchEngines } }
            .launchIn(CoroutineScope(IO))
    }

    // ------------------------------------------------------------------------------------ //

    suspend fun addSearchEngine(engine: SearchEngine) = withContext(IO) {
        dao.insert(engine)
    }

    suspend fun updateSearchEngine(engine: SearchEngine) = withContext(IO) {
        dao.update(engine)
    }

    suspend fun deleteSearchEngine(engine: SearchEngine) = withContext(IO) {
        dao.delete(engine)
    }
}