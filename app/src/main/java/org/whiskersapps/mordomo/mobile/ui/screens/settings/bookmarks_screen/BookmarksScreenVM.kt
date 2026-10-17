package org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.BookmarksRepository
import org.whiskersapps.mordomo.mobile.core.utils.isFullURL
import org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.BookmarksScreenState.AddBookmarkDialogState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.BookmarksScreenState.EditBookmarkDialogState

class BookmarksScreenVM(
    val bookmarksRepository: BookmarksRepository,
) : ViewModel() {
    private val screenIntent = ScreenIntent()
    private val addBookmarkIntent = AddBookmarkIntent()
    private val editBookmarkIntent = EditBookmarkIntent()

    private val _state = MutableStateFlow(BookmarksScreenState())
    val state = _state.asStateFlow()

    private var pendingBookmark: Bookmark? = null
    private var preDeletionBookmarks: List<Bookmark> = emptyList()

    init {
        bookmarksRepository.bookmarks.onEach { bookmarks ->
            _state.update {
                it.copy(
                    loading = false,
                    bookmarks = bookmarks,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: BookmarksScreenIntent) {
        when (intent) {
            BookmarksScreenIntent.Back -> screenIntent.onBack()
            is BookmarksScreenIntent.BookmarkClick -> screenIntent.onBookmarkClick(intent.bookmark)
            is BookmarksScreenIntent.FABClick -> screenIntent.onFABClick()
            is BookmarksScreenIntent.EditSnackResult -> screenIntent.onEditSnackResult(intent.result)

            is AddBookmarkDialogIntent -> when (intent) {
                is AddBookmarkDialogIntent.NameType -> addBookmarkIntent.onNameType(intent.text)
                is AddBookmarkDialogIntent.URLType -> addBookmarkIntent.onURLType(intent.text)
                AddBookmarkDialogIntent.SaveClick -> addBookmarkIntent.onSaveClick()
                AddBookmarkDialogIntent.Close -> addBookmarkIntent.onClose()
            }

            is EditBookmarkDialogIntent -> when (intent) {
                is EditBookmarkDialogIntent.NameType -> editBookmarkIntent.onNameType(intent.text)
                is EditBookmarkDialogIntent.URLType -> editBookmarkIntent.onURLType(intent.text)
                EditBookmarkDialogIntent.DeleteClick -> editBookmarkIntent.onDeleteSlide()
                EditBookmarkDialogIntent.SaveClick -> editBookmarkIntent.onSaveClick()
                EditBookmarkDialogIntent.Close -> editBookmarkIntent.onClose()
            }

//            else -> throw Exception("Invalid Action")
        }
    }

    // --------------------------------- Intents ------------------------------ //

    private inner class ScreenIntent {
        fun onBack() {
            viewModelScope.launch {
                pendingBookmark?.let { bookmark ->
                    bookmarksRepository.deleteBookmark(bookmark)
                    pendingBookmark = null
                }
            }
        }

        fun onFABClick() {
            _state.update { it.copy(addBookmarkDialogState = AddBookmarkDialogState(show = true)) }
        }

        fun onBookmarkClick(bookmark: Bookmark) {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        editBookmarkDialogState = EditBookmarkDialogState(
                            show = true,
                            name = bookmark.name,
                            url = bookmark.url,
                        )
                    )
                }

                pendingBookmark = bookmark
            }
        }

        fun onEditSnackResult(result: SnackbarResult) {
            viewModelScope.launch {
                when (result) {
                    SnackbarResult.Dismissed -> {
                        bookmarksRepository.deleteBookmark(pendingBookmark!!)
                        pendingBookmark = null
                    }

                    SnackbarResult.ActionPerformed -> {
                        _state.update { it.copy(bookmarks = preDeletionBookmarks) }
                        pendingBookmark = null
                    }
                }

                _state.update {
                    it.copy(
                        showUndoSnack = false,
                        editBookmarkDialogState = EditBookmarkDialogState()
                    )
                }
            }
        }
    }

    private inner class AddBookmarkIntent {
        fun onNameType(text: String) {
            _state.update {
                it.copy(
                    addBookmarkDialogState = it.addBookmarkDialogState.copy(
                        name = text,
                        nameError = null
                    )
                )
            }
        }

        fun onURLType(text: String) {
            _state.update {
                it.copy(
                    addBookmarkDialogState = it.addBookmarkDialogState.copy(
                        url = text,
                        urlError = null
                    )
                )
            }
        }

        fun onSaveClick() {
            viewModelScope.launch {
                val name = state.value.addBookmarkDialogState.name
                val url = state.value.addBookmarkDialogState.url

                val nameError = if (name.isNotBlank()) null else "Name must not be empty"
                val urlError = if (url.isFullURL()) null else "URL must be a valid one"

                if (nameError != null || urlError != null) {
                    _state.update {
                        it.copy(
                            addBookmarkDialogState = it.addBookmarkDialogState.copy(
                                nameError = nameError,
                                urlError = urlError
                            )
                        )
                    }

                    return@launch
                }

                val bookmark = Bookmark(
                    name = state.value.addBookmarkDialogState.name,
                    url = state.value.addBookmarkDialogState.url
                )

                closeDialog()

                bookmarksRepository.addBookmark(bookmark)
            }
        }

        fun onClose() = closeDialog()

        private fun closeDialog() {
            _state.update { it.copy(addBookmarkDialogState = AddBookmarkDialogState()) }
        }
    }

    private inner class EditBookmarkIntent {
        fun onNameType(text: String) {
            _state.update {
                it.copy(
                    editBookmarkDialogState = it.editBookmarkDialogState.copy(
                        name = text,
                        nameError = null
                    )
                )
            }
        }

        fun onURLType(text: String) {
            _state.update {
                it.copy(
                    editBookmarkDialogState = it.editBookmarkDialogState.copy(
                        url = text,
                        urlError = null
                    )
                )
            }
        }

        fun onDeleteSlide() {
            viewModelScope.launch {
                preDeletionBookmarks = state.value.bookmarks

                _state.update {
                    it.copy(
                        showUndoSnack = true,
                        bookmarks = it.bookmarks.filter { bm -> bm.id != pendingBookmark!!.id },
                        editBookmarkDialogState = it.editBookmarkDialogState.copy(show = false)
                    )
                }
            }
        }

        fun onSaveClick() {
            viewModelScope.launch {
                val name = state.value.editBookmarkDialogState.name
                val url = state.value.editBookmarkDialogState.url

                val nameError = if (name.isNotBlank()) null else "Name must not be empty"
                val urlError = if (url.isFullURL()) null else "URL must be a valid one"

                if (nameError != null || urlError != null) {
                    _state.update {
                        it.copy(
                            editBookmarkDialogState = it.editBookmarkDialogState.copy(
                                nameError = nameError,
                                urlError = urlError
                            )
                        )
                    }

                    return@launch
                }

                val bookmark = pendingBookmark!!.copy(
                    name = name,
                    url = url
                )

                closeDialog()

                bookmarksRepository.updateBookmark(bookmark)
            }
        }

        fun onClose() = closeDialog()

        private fun closeDialog() {
            _state.update { it.copy(editBookmarkDialogState = EditBookmarkDialogState()) }
            pendingBookmark = null
        }
    }
}