package org.whiskersapps.mordomo.mobile.core.features.bookmarks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val url: String,
)
