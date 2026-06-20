package org.whiskersapps.mordomo.mobile.ui.main_screen.add_bookmark


data class AddBookmarkSharingScreenState(
    val name: String,
    val url: String,
    val saveEnabled: Boolean = false,
    val killFlag: Boolean = false
)