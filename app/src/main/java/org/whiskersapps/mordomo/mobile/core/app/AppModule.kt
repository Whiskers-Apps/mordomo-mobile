package org.whiskersapps.mordomo.mobile.core.app

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.whiskersapps.mordomo.mobile.core.db.getDB
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.BookmarksRepository
import org.whiskersapps.mordomo.mobile.core.features.icons.IconsRepository
import org.whiskersapps.mordomo.mobile.core.features.lock.LockRepository
import org.whiskersapps.mordomo.mobile.core.features.screen.FoldableRepository
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEnginesRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDoRepository
import org.whiskersapps.mordomo.mobile.ui.screens.add_bookmark.AddBookmarkSharingScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.AppsScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.HomeScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.SearchScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.SettingsScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen.AboutScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.apps_screen.AppsSettingsScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.BookmarksScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.home_screen.HomeSettingsScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen.LockSettingsScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen.QuickPanelSettingsScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.SearchEnginesScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_screen.SearchSettingsScreenVM
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenVM

val appModule = module {

    // Database Singleton
    single { getDB(get()) }

    // --------------------- Repositories -------------------------- //
    single(createdAtStart = true) { SettingsRepository(get()) }
    single(createdAtStart = true) { IconsRepository(get()) }
    single(createdAtStart = true) { AppsRepository(get(), get(), get()) }
    single(createdAtStart = true) { BookmarksRepository(get()) }
    single(createdAtStart = true) { SearchEnginesRepository(get()) }
    single(createdAtStart = true) { FoldableRepository() }
    single(createdAtStart = true) { ToDoRepository(get()) }
    single { LockRepository(get(), get()) }


    // -------------------- View Models ----------------------- //
    viewModel { QuickPanelScreenVM(get(), get(), get(), get()) }
    viewModel { HomeScreenVM(get(), get(), get()) }
    viewModel { SearchScreenVM(get(), get(), get(), get(), get()) }
    viewModel { AppsScreenVM(get(), get()) }

    viewModel { SettingsScreenVM(get()) }
    viewModel { QuickPanelSettingsScreenVM(get()) }
    viewModel { HomeSettingsScreenVM(get()) }
    viewModel { AppsSettingsScreenVM(get()) }
    viewModel { SearchSettingsScreenVM(get()) }
    viewModel { BookmarksScreenVM(get()) }
    viewModel { SearchEnginesScreenVM(get(), get()) }
    viewModel { SecurityScreenVM(get()) }
    viewModel { LockSettingsScreenVM(get(), get()) }
    viewModel { AboutScreenVM(get()) }

    viewModel { params ->
        AddBookmarkSharingScreenVM(
            name = params.get(),
            url = params.get(),
            bookmarksRepository = get()
        )
    }

}