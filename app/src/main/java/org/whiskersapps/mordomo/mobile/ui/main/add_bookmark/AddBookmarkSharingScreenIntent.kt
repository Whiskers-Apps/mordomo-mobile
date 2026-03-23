package org.whiskersapps.mordomo.mobile.ui.main.add_bookmark

sealed interface AddBookmarkSharingScreenIntent {
    data class NameType(val text: String) : AddBookmarkSharingScreenIntent
    data class URLType(val text: String) : AddBookmarkSharingScreenIntent

    data object CancelClick: AddBookmarkSharingScreenIntent

    data object SaveClick: AddBookmarkSharingScreenIntent
}