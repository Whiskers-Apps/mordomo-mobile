package org.whiskersapps.mordomo.mobile.ui.navigation.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.components.SettingSection
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
    SettingsScreen(state = vm.state.collectAsState().value) { action ->
        when (action) {
            SettingsScreenAction.AboutSectionClick -> onGoToAboutScreen()
            SettingsScreenAction.AppsSectionClick -> onGoToAppsScreen()
            SettingsScreenAction.HomeSectionClick -> onGoToHomeScreen()
            SettingsScreenAction.BookmarksSectionClick -> onGoToBookmarksScreen()
            SettingsScreenAction.SearchEnginesSectionClick -> onGoToSearchEnginesScreen()
            SettingsScreenAction.SecuritySectionClick -> onGoToSecurityScreen()
            SettingsScreenAction.LockSectionClick -> onGoToLockScreen()
            SettingsScreenAction.StyleSectionClick -> onGoToStyleScreen()
            else -> vm.onAction(action)
        }
    }
}

@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    onAction: (SettingsScreenAction) -> Unit
) {
    if (!state.loading) {
        ResponsiveColumn(
            contentPadded = true,
            scrollable = true
        ) {
            if (!state.isDefaultLauncher) {
                SettingSection(
                    shape = SingleCardShape,
                    iconId = R.drawable.info,
                    title = "Default Launcher",
                    description = "Click here to set Mordomo as the default launcher",
                    onClick = { onAction(SettingsScreenAction.SetDefaultLauncherClick) }
                )

                Spacer(Modifier.height(16.dp))
            }

            SettingSection(
                shape = TopCardShape,
                iconId = R.drawable.palette,
                title = "Style",
                description = "Customize the app theme and icons",
                onClick = { onAction(SettingsScreenAction.StyleSectionClick) }
            )

            Spacer(Modifier.height(2.dp))

            SettingSection(
                shape = MiddleCardShape,
                iconId = R.drawable.home,
                title = "Home",
                description = "Customize home buttons and clock",
                onClick = { onAction(SettingsScreenAction.HomeSectionClick) }
            )

            Spacer(Modifier.height(2.dp))

            SettingSection(
                shape = BottomCardShape,
                iconId = R.drawable.apps_outline,
                title = "Apps",
                description = "Customize apps layout and search",
                onClick = { onAction(SettingsScreenAction.AppsSectionClick) }
            )

            Spacer(Modifier.height(16.dp))

            SettingSection(
                shape = TopCardShape,
                iconId = R.drawable.bookmark,
                title = "Bookmarks",
                description = "Add bookmarks to quickly open links",
                onClick = { onAction(SettingsScreenAction.BookmarksSectionClick) }
            )


            Spacer(Modifier.height(2.dp))

            SettingSection(
                shape = BottomCardShape,
                iconId = R.drawable.search,
                title = "Search Engines",
                description = "Add search engines to search",
                onClick = { onAction(SettingsScreenAction.SearchEnginesSectionClick) }
            )

            Spacer(Modifier.height(16.dp))

            SettingSection(
                shape = TopCardShape,
                iconId = R.drawable.fingerprint,
                title = "Security",
                description = "Hide and secure apps with fingerprint",
                onClick = { onAction(SettingsScreenAction.SecuritySectionClick) }
            )


            Spacer(Modifier.height(2.dp))

            SettingSection(
                shape = BottomCardShape,
                iconId = R.drawable.lock,
                title = "Lock",
                description = "Manage screen locking gesture",
                onClick = { onAction(SettingsScreenAction.LockSectionClick) }
            )

            Spacer(Modifier.height(16.dp))

            SettingSection(
                shape = SingleCardShape,
                iconId = R.drawable.info,
                title = "About",
                description = "Information about this app",
                onClick = { onAction(SettingsScreenAction.LockSectionClick) }
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Preview() {
    SettingsScreen(state = SettingsScreenState(loading = false, isDefaultLauncher = true)) {}
}