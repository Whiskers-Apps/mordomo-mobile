package org.whiskersapps.mordomo.mobile.ui.screens.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.screens.settings.components.SettingsSection
import org.whiskersapps.mordomo.mobile.ui.shared_composables.BottomCardShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.MiddleCardShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.SingleCardShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TopCardShape


@Composable
fun SettingsScreenRoot(
    onGoToStyleScreen: () -> Unit,
    onGoToHomeScreen: () -> Unit,
    onGoToAppsScreen: () -> Unit,
    onGoToBookmarksScreen: () -> Unit,
    onGoToSearchEnginesScreen: () -> Unit,
    onGoToSecurityScreen: () -> Unit,
    onGoToLockScreen: () -> Unit,
    onGoToAboutScreen: () -> Unit,
    vm: SettingsScreenVM = koinViewModel()
) {
    SettingsScreen(state = vm.state.collectAsState().value) { intent ->
        when (intent) {
            SettingsScreenIntent.AboutSectionClick -> onGoToAboutScreen()
            SettingsScreenIntent.AppsSectionClick -> onGoToAppsScreen()
            SettingsScreenIntent.HomeSectionClick -> onGoToHomeScreen()
            SettingsScreenIntent.BookmarksSectionClick -> onGoToBookmarksScreen()
            SettingsScreenIntent.SearchEnginesSectionClick -> onGoToSearchEnginesScreen()
            SettingsScreenIntent.SecuritySectionClick -> onGoToSecurityScreen()
            SettingsScreenIntent.LockSectionClick -> onGoToLockScreen()
            SettingsScreenIntent.StyleSectionClick -> onGoToStyleScreen()
            else -> vm.onIntent(intent)
        }
    }
}

@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    onIntent: (SettingsScreenIntent) -> Unit
) {
    if (!state.loading) {
        ResponsiveColumn(
            contentPadded = true,
            scrollable = true
        ) {
            if (!state.isDefaultLauncher) {
                SettingsSection(
                    shape = SingleCardShape,
                    iconId = R.drawable.info,
                    title = "Default Launcher",
                    description = "Click here to set Mordomo as the default launcher",
                    onClick = { onIntent(SettingsScreenIntent.SetDefaultLauncherClick) }
                )

                Spacer(Modifier.height(16.dp))
            }

            SettingsSection(
                shape = TopCardShape,
                iconId = R.drawable.palette,
                title = "Style",
                description = "Customize the app theme and icons",
                onClick = { onIntent(SettingsScreenIntent.StyleSectionClick) }
            )

            Spacer(Modifier.height(2.dp))

            SettingsSection(
                shape = MiddleCardShape,
                iconId = R.drawable.home,
                title = "Home",
                description = "Customize home buttons and clock",
                onClick = { onIntent(SettingsScreenIntent.HomeSectionClick) }
            )

            Spacer(Modifier.height(2.dp))

            SettingsSection(
                shape = BottomCardShape,
                iconId = R.drawable.apps_outline,
                title = "Apps",
                description = "Customize apps layout and search",
                onClick = { onIntent(SettingsScreenIntent.AppsSectionClick) }
            )

            Spacer(Modifier.height(16.dp))

            SettingsSection(
                shape = TopCardShape,
                iconId = R.drawable.bookmark_outline,
                title = "Bookmarks",
                description = "Add bookmarks to quickly open links",
                onClick = { onIntent(SettingsScreenIntent.BookmarksSectionClick) }
            )


            Spacer(Modifier.height(2.dp))

            SettingsSection(
                shape = BottomCardShape,
                iconId = R.drawable.search,
                title = "Search Engines",
                description = "Add search engines to search",
                onClick = { onIntent(SettingsScreenIntent.SearchEnginesSectionClick) }
            )

            Spacer(Modifier.height(16.dp))

            SettingsSection(
                shape = TopCardShape,
                iconId = R.drawable.fingerprint,
                title = "Security",
                description = "Hide and secure apps with fingerprint",
                onClick = { onIntent(SettingsScreenIntent.SecuritySectionClick) }
            )


            Spacer(Modifier.height(2.dp))

            SettingsSection(
                shape = BottomCardShape,
                iconId = R.drawable.lock_outline,
                title = "Lock",
                description = "Manage screen locking gesture",
                onClick = { onIntent(SettingsScreenIntent.LockSectionClick) }
            )

            Spacer(Modifier.height(16.dp))

            SettingsSection(
                shape = SingleCardShape,
                iconId = R.drawable.info,
                title = "About",
                description = "Information about this app",
                onClick = { onIntent(SettingsScreenIntent.AboutSectionClick) }
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Preview() {
    SettingsScreen(state = SettingsScreenState(loading = false, isDefaultLauncher = true)) {}
}