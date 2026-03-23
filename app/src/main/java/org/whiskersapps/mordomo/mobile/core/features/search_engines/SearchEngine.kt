package org.whiskersapps.mordomo.mobile.core.features.search_engines

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchEngine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val query: String
)
