package org.whiskersapps.mordomo.mobile.core.features.search_engines

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SearchEnginesDao {
    @Query("SELECT * FROM searchengine")
    fun getAll(): List<SearchEngine>

    @Insert
    fun insert(searchEngine: SearchEngine)

    @Insert
    fun insertMultiple(searchEngines: List<SearchEngine>)

    @Update
    fun update(searchEngine: SearchEngine)

    @Delete
    fun delete(searchEngine: SearchEngine)
}