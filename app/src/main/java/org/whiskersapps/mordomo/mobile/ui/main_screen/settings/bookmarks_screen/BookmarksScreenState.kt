package org.whiskersapps.mordomo.mobile.ui.main_screen.settings.bookmarks_screen

import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark

data class BookmarksScreenState(
    val loading: Boolean = false,
    val bookmarks: List<Bookmark> = emptyList(),
    val addBookmarkDialogState: AddBookmarkDialogState = AddBookmarkDialogState(),
    val editBookmarkDialogState: EditBookmarkDialogState = EditBookmarkDialogState(),
    val showUndoSnack: Boolean = false
) {
    data class AddBookmarkDialogState(
        val show: Boolean = false,
        val name: String = "",
        val url: String = "",
        val saveEnabled: Boolean = false
    )

    data class EditBookmarkDialogState(
        val show: Boolean = false,
        val name: String = "",
        val url: String = "",
        val saveEnabled: Boolean = false
    )
}
