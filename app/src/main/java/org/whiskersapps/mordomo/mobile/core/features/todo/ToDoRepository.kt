package org.whiskersapps.mordomo.mobile.core.features.todo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.db.AppDB

class ToDoRepository(val db: AppDB) {
    val dao = db.toDoDao()

    private val _toDos = MutableStateFlow<List<ToDo>>(emptyList())
    val toDos = _toDos.asStateFlow()

    init {
        dao.getAll()
            .onEach { tds -> _toDos.update { tds } }
            .launchIn(CoroutineScope(IO))
    }

    suspend fun addToDo(toDo: ToDo) = withContext(IO) {
        dao.insert(toDo)
    }

    suspend fun deleteToDo(id: Int) = withContext(IO) {
        dao.delete(id)
    }

    suspend fun toggleToDo(id: Int) = withContext(IO) {
        dao.toggleToDo(id)
    }
}