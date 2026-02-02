package org.whiskersapps.mordomo.mobile.core.app

import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import org.whiskersapps.mordomo.mobile.core.features.apps.AppsRepository
import org.whiskersapps.mordomo.mobile.core.features.icons.IconsRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.AppsScreenVM

val appModule = module {

    // Repositories
    single { SettingsRepository(get()) } withOptions { createdAtStart() }
    single { IconsRepository(get()) } withOptions { createdAtStart() }
    single { AppsRepository(get(), get()) } withOptions { createdAtStart() }

    // Screens ViewModels
    viewModel { AppsScreenVM(get(), get()) }
}