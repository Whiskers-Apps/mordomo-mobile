package org.whiskersapps.mordomo.mobile.ui.main.settings.bookmarks_screen

import androidx.compose.material3.SnackbarResult
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark

sealed interface BookmarksScreenIntent {
    data object Back : BookmarksScreenIntent
    data object FABClick : BookmarksScreenIntent
    data class BookmarkClick(val bookmark: Bookmark) : BookmarksScreenIntent
    data class EditSnackResult(val result: SnackbarResult) : BookmarksScreenIntent
}

sealed interface AddBookmarkDialogIntent : BookmarksScreenIntent {
    data class NameType(val text: String) : AddBookmarkDialogIntent
    data class URLType(val text: String) : AddBookmarkDialogIntent
    data object SaveClick : AddBookmarkDialogIntent
    data object Close : AddBookmarkDialogIntent
}

sealed interface EditBookmarkDialogIntent : BookmarksScreenIntent {
    data class NameType(val text: String) : EditBookmarkDialogIntent
    data class URLType(val text: String) : EditBookmarkDialogIntent
    data object DeleteSlide : EditBookmarkDialogIntent
    data object SaveClick : EditBookmarkDialogIntent
    data object Close : EditBookmarkDialogIntent
}
