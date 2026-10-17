package org.whiskersapps.mordomo.mobile.ui.screens.add_bookmark


data class AddBookmarkSharingScreenState(
    val name: String,
    val nameError: String? = null,
    val url: String,
    val urlError: String? = null,
    val killFlag: Boolean = false
)