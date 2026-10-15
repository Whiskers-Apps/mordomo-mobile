package org.whiskersapps.mordomo.mobile.core.features.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM ToDo")
    fun getAll(): Flow<List<ToDo>>

    @Query("""UPDATE ToDo SET done = NOT done WHERE id = :id""")
    fun toggleToDo(id: Int)

    @Query("""DELETE from ToDo WHERE id = :id""")
    fun delete(id: Int)

    @Insert
    fun insert(vararg toDo: ToDo)

    @Update
    fun update(vararg toDo: ToDo)
}