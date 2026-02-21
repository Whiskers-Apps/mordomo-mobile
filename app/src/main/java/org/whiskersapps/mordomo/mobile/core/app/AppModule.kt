package org.whiskersapps.mordomo.mobile.core.app

import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.icons.IconsRepository
import org.whiskersapps.mordomo.mobile.core.features.screen.FoldableRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.AppsScreenVM
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.HomeScreenVM
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.SettingsScreenVM
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.home_screen.HomeSettingsScreenVM

val appModule = module {

    // Repositories
    single { SettingsRepository(get()) } withOptions { createdAtStart() }
    single { IconsRepository(get()) } withOptions { createdAtStart() }
    single { AppsRepository(get(), get()) } withOptions { createdAtStart() }
    single { FoldableRepository() } withOptions { createdAtStart() }

    // Screens ViewModels
    viewModel { HomeScreenVM(get(), get()) }
    viewModel { AppsScreenVM(get(), get()) }

    viewModel { SettingsScreenVM(get()) }
    viewModel { HomeSettingsScreenVM(get()) }

}