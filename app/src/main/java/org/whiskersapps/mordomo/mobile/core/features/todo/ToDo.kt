package org.whiskersapps.mordomo.mobile.core.features.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val done: Boolean = false,
)