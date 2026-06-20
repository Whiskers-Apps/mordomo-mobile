package org.whiskersapps.mordomo.mobile.ui.main_screen.add_bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.BookmarksRepository
import org.whiskersapps.mordomo.mobile.core.utils.isFullURL
import org.whiskersapps.mordomo.mobile.ui.main_screen.add_bookmark.AddBookmarkSharingScreenState as ScreenState

class AddBookmarkSharingScreenVM(
    name: String,
    url: String,
    val bookmarksRepository: BookmarksRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState(name = name, url = url))
    val state = _state.asStateFlow()

    init {
        verifyFields()
    }

    fun onIntent(intent: AddBookmarkSharingScreenIntent) {
        when (intent) {
            is AddBookmarkSharingScreenIntent.NameType -> onNameType(intent.text)
            is AddBookmarkSharingScreenIntent.URLType -> onURLType(intent.text)
            AddBookmarkSharingScreenIntent.SaveClick -> onSave()
            AddBookmarkSharingScreenIntent.CancelClick -> onCancel()
        }
    }

    private fun onNameType(text: String) {
        _state.update { it.copy(name = text) }
        verifyFields()
    }

    private fun onURLType(text: String) {
        _state.update { it.copy(url = text) }
        verifyFields()
    }

    private fun onSave() {
        viewModelScope.launch {
            val bookmark = Bookmark(
                name = state.value.name,
                url = state.value.url
            )

            bookmarksRepository.addBookmark(bookmark)

            killActivity()
        }
    }

    private fun onCancel() = killActivity()

    private fun killActivity() {
        _state.update { it.copy(killFlag = true) }
    }

    private fun verifyFields() {
        val name = state.value.name
        val url = state.value.url
        val enabled = name.isNotBlank() && url.isFullURL()

        _state.update { it.copy(saveEnabled = enabled) }
    }
}