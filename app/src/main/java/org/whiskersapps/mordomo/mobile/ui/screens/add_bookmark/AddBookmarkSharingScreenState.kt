package org.whiskersapps.mordomo.mobile.ui.screens.add_bookmark


data class AddBookmarkSharingScreenState(
    val name: String,
    val url: String,
    val saveEnabled: Boolean = false,
    val killFlag: Boolean = false
)