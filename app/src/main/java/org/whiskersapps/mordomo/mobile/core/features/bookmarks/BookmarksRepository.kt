package org.whiskersapps.mordomo.mobile.core.features.bookmarks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.db.AppDB

class BookmarksRepository(db: AppDB) {
    private val dao = db.bookmarksDao()

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks = _bookmarks.asStateFlow()


    init {
        dao.getAll()
            .onEach { bms -> _bookmarks.update { bms } }
            .launchIn(CoroutineScope(IO))
    }

    suspend fun addBookmark(bookmark: Bookmark) = withContext(IO) {
        dao.insert(bookmark)
    }

    suspend fun updateBookmark(bookmark: Bookmark) = withContext(IO){
        dao.update(bookmark)
    }

    suspend fun deleteBookmark(bookmark: Bookmark) = withContext(IO) {
        dao.delete(bookmark)
    }
}