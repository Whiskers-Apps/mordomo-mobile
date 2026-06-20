package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiskersapps.lib.Sniffer
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.BookmarksRepository
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEnginesRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.core.utils.isShortURL
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.SearchScreenState.AppDialogState
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.SearchScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.SearchScreenUICallback as UICallback

class SearchScreenVM(
    val context: Application,
    val appsRepository: AppsRepository,
    settingsRepository: SettingsRepository,
    val bookmarksRepository: BookmarksRepository,
    searchEnginesRepository: SearchEnginesRepository
) : ViewModel() {
    private val screenHandler = ScreenHandler()
    private val appDialogHandler = AppDialogHandler()

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    private val _uiCallbacks = Channel<UICallback>()
    val uiCallbacks = _uiCallbacks.receiveAsFlow()

    private val sniffer = Sniffer()

    init {
        combine(
            settingsRepository.appSettings,
            searchEnginesRepository.searchEngines
        ) { appSettings, searchEngines ->
            _state.update {
                it.copy(
                    loading = false,
                    searchEngine = when (appSettings.search.defaultSearchEngine) {
                        null -> {
                            null
                        }

                        else -> {
                            searchEngines.firstOrNull { se -> se.id == appSettings.search.defaultSearchEngine }
                        }
                    }
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: ScreenIntent) {
        when (intent) {
            is ScreenIntent.SearchType -> screenHandler.onSearchType(intent.text)
            ScreenIntent.SearchDone -> screenHandler.onSearchDone()
            ScreenIntent.SheetExpand -> screenHandler.onSheetExpand()
            ScreenIntent.SheetClose -> screenHandler.onSheetClose()
            ScreenIntent.FocusClear -> screenHandler.onFocusClear()
            is ScreenIntent.AppClick -> screenHandler.onAppClick(intent.app)
            is ScreenIntent.AppLongClick -> screenHandler.onAppLongClick(intent.app)
            is ScreenIntent.BookmarkClick -> screenHandler.onBookmarkClick(intent.bookmark)
            is ScreenIntent.WebSearchClick -> screenHandler.onWebSearchClick()

            is AppDialogIntent -> when (intent) {
                is AppDialogIntent.InfoClick -> appDialogHandler.onInfoClick()
                is AppDialogIntent.ShortcutClick -> appDialogHandler.onShortcutClick(intent.shortcut)
                is AppDialogIntent.UninstallClick -> appDialogHandler.onUninstallClick()
                AppDialogIntent.Close -> appDialogHandler.onClose()
            }
        }
    }

    private inner class ScreenHandler {
        fun onSearchType(text: String) {
            _state.update { it.copy(searchText = text) }

            viewModelScope.launch {
                val filteredApps = if (text.trim().isEmpty()) {
                    emptyList()
                } else {
                    appsRepository.filteredApps.value.filter { app ->
                        sniffer.matches(app.name, text)
                    }
                }.take(6)

                val filteredBookmarks = if (text.isBlank()) {
                    emptyList()
                } else {
                    bookmarksRepository.bookmarks.value.filter { bookmark ->
                        sniffer.matches(bookmark.name, text)
                    }
                }.take(6)


                withContext(Main) {
                    _state.update {
                        it.copy(
                            apps = filteredApps,
                            bookmarks = filteredBookmarks,
                            circleApps = filteredApps.size == 1,
                            circleBookmarks = filteredBookmarks.size == 1,
                            isTextURL = text.isShortURL()
                        )
                    }
                }
            }
        }

        fun onSearchDone() {
            viewModelScope.launch {
                val firstApp = state.value.apps.firstOrNull()
                val firstBookmark = state.value.bookmarks.firstOrNull()

                firstApp?.let { app ->
                    appsRepository.openApp(app)
                    resetScreen()
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

                        resetScreen()
                    } catch (e: Exception) {
                        Log.e("Search Screen", "Error opening bookmark")
                        e.printStackTrace()
                    }
                }

                if (state.value.isTextURL) {
                    openURL(state.value.searchText)
                    resetScreen()
                }

                state.value.searchEngine?.let { searchEngine ->
                    val queryURL = searchEngine.query
                    val searchURL = queryURL.replace("%s", state.value.searchText)

                    openURL(searchURL)

                    resetScreen()
                }
            }
        }

        fun onSheetExpand() {
            viewModelScope.launch {
                _uiCallbacks.send(UICallback.FocusSearchBar)
            }
        }

        fun onSheetClose() = resetScreen()

        fun onFocusClear() {
            viewModelScope.launch {
                _uiCallbacks.send(UICallback.ClearFocus)
            }
        }

        fun onAppClick(app: App) {
            viewModelScope.launch {
                appsRepository.openApp(app)

                _state.update {
                    it.copy(
                        searchText = "",
                        apps = emptyList()
                    )
                }

                resetScreen()
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
            openURL(bookmark.url)

            resetScreen()
        }

        fun onWebSearchClick() {
            if (state.value.isTextURL) {
                openURL(state.value.searchText)
                resetScreen()

                return
            }

            val queryURL = state.value.searchEngine!!.query
            val searchURL = queryURL.replace("%s", state.value.searchText)

            openURL(searchURL)
            resetScreen()
        }


        private fun openURL(url: String) {
            try {
                val parsedURL = if (!url.startsWith("https") || !url.startsWith("http")) {
                    "https://${url}"
                } else {
                    url
                }

                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        parsedURL.toUri()
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )

                resetScreen()
            } catch (e: Exception) {
                Log.e("Search Screen", "Error opening url")
                e.printStackTrace()
            }
        }
    }

    private inner class AppDialogHandler {
        fun onInfoClick() {
            viewModelScope.launch {
                appsRepository.openAppInfo(state.value.appDialogState.app!!)
                closeDialog()

                resetScreen()
            }
        }

        fun onUninstallClick() {
            viewModelScope.launch {
                appsRepository.requestUninstall(state.value.appDialogState.app!!)
                closeDialog()

                resetScreen()
            }
        }

        fun onShortcutClick(shortcut: App.Shortcut) {
            viewModelScope.launch {
                appsRepository.openShortcut(state.value.appDialogState.app!!, shortcut)
                closeDialog()

                resetScreen()
            }
        }

        fun onClose() = closeDialog()

        private fun closeDialog() {
            _state.update {
                it.copy(appDialogState = AppDialogState())
            }
        }
    }

    private fun resetScreen() {
        _state.update {
            it.copy(
                searchText = "",
                apps = emptyList(),
                bookmarks = emptyList(),
                circleApps = false,
                circleBookmarks = false,
            )
        }

        viewModelScope.launch {
            _uiCallbacks.send(UICallback.ClearFocus)
            _uiCallbacks.send(UICallback.HideKeyboard)
            _uiCallbacks.send(UICallback.CloseSheet)
        }
    }
}