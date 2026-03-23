package org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.search_screen

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiskersapps.lib.Sniffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.BookmarksRepository
import org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.search_screen.SearchScreenState.AppDialogState
import org.whiskersapps.mordomo.mobile.ui.main.launcher.home_screen.search_screen.SearchScreenIntent as ScreenIntent

class SearchScreenVM(
    val context: Application,
    val appsRepository: AppsRepository,
    val bookmarksRepository: BookmarksRepository
) : ViewModel() {
    private val screenIntents = ScreenIntents()
    private val appDialogIntents = AppDialogIntents()

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    private val sniffer = Sniffer()

    private val apps = appsRepository.filteredApps
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val bookmarks =
        bookmarksRepository.bookmarks.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun onIntent(intent: ScreenIntent) {
        when (intent) {
            is ScreenIntent.SearchType -> screenIntents.onSearchType(intent.text)
            ScreenIntent.SearchDone -> screenIntents.onSearchDone()
            ScreenIntent.SheetExpand -> screenIntents.onSheetExpand()
            ScreenIntent.SheetClose -> screenIntents.onSheetClose()
            ScreenIntent.FocusClear -> screenIntents.onFocusClear()
            is ScreenIntent.AppClick -> screenIntents.onAppClick(intent.app)
            is ScreenIntent.AppLongClick -> screenIntents.onAppLongClick(intent.app)
            is ScreenIntent.BookmarkClick -> screenIntents.onBookmarkClick(intent.bookmark)

            is AppDialogIntent -> when (intent) {
                is AppDialogIntent.InfoClick -> appDialogIntents.onInfoClick()
                is AppDialogIntent.ShortcutClick -> appDialogIntents.onShortcutClick(intent.shortcut)
                is AppDialogIntent.UninstallClick -> appDialogIntents.onUninstallClick()
                AppDialogIntent.Close -> appDialogIntents.onClose()
            }

            else -> throw Exception("Invalid Intent")
        }
    }

    // -------------------------- Intents --------------------------------- //

    private inner class ScreenIntents {
        fun onSearchType(text: String) {
            _state.update { it.copy(searchText = text) }

            viewModelScope.launch {
                val filteredApps = if (text.trim().isEmpty()) {
                    emptyList()
                } else {
                    apps.value.filter { app ->
                        sniffer.matches(app.name, text)
                    }
                }.take(6)

                val filteredBookmarks = if (text.isBlank()) {
                    emptyList()
                } else {
                    bookmarks.value.filter { bookmark ->
                        sniffer.matches(bookmark.name, text)
                    }
                }.take(6)


                withContext(Main) {
                    _state.update { it.copy(apps = filteredApps, bookmarks = filteredBookmarks) }
                }
            }
        }

        fun onSearchDone() {
            viewModelScope.launch {
                val firstApp = state.value.apps.firstOrNull()
                val firstBookmark = state.value.bookmarks.firstOrNull()

                firstApp?.let { app ->
                    appsRepository.openApp(app)
                    closeSheet()
                    return@launch
                }

                firstBookmark?.let { bookmark ->
                    try {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                bookmark.url.toUri()
                            ).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        )

                        closeSheet()
                    } catch (e: Exception) {
                        Log.e("Search Screen", "Error opening bookmark")
                        e.printStackTrace()
                    }
                }
            }
        }

        fun onSheetExpand() {
            _state.update { it.copy(focusSearchBar = true) }
        }

        fun onSheetClose() = closeSheet()

        fun onFocusClear() {
            _state.update { it.copy(focusSearchBar = false) }
        }

        fun onAppClick(app: App) {
            viewModelScope.launch {
                appsRepository.openApp(app)

                _state.update {
                    it.copy(
                        focusSearchBar = false,
                        searchText = "",
                        apps = emptyList()
                    )
                }
            }
        }

        fun onAppLongClick(app: App) {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        appDialogState = AppDialogState(
                            show = true,
                            app = app
                        )
                    )
                }
            }
        }

        fun onBookmarkClick(bookmark: Bookmark) {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        bookmark.url.toUri()
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )

                closeSheet()
            } catch (e: Exception) {
                Log.e("Search Screen", "Error opening bookmark")
                e.printStackTrace()
            }
        }

        private fun closeSheet() {
            _state.update {
                it.copy(
                    focusSearchBar = false,
                    searchText = "",
                    apps = emptyList(),
                    bookmarks = emptyList()
                )
            }
        }
    }

    private inner class AppDialogIntents {
        fun onInfoClick() {
            viewModelScope.launch {
                appsRepository.openAppInfo(state.value.appDialogState.app!!)
                closeDialog()
            }
        }

        fun onUninstallClick() {
            viewModelScope.launch {
                appsRepository.requestUninstall(state.value.appDialogState.app!!)
                closeDialog()
            }
        }

        fun onShortcutClick(shortcut: App.Shortcut) {
            viewModelScope.launch {
                appsRepository.openShortcut(state.value.appDialogState.app!!, shortcut)
                closeDialog()
            }
        }

        fun onClose() = closeDialog()

        private fun closeDialog() {
            _state.update {
                it.copy(appDialogState = AppDialogState())
            }
        }
    }


}