package org.whiskersapps.mordomo.mobile.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.screens.settings.components.SettingsSection
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section


@Composable
fun SettingsScreenRoot(
    onGoToStyleScreen: () -> Unit,
    onGoToQuickPanelScreen: () -> Unit,
    onGoToHomeScreen: () -> Unit,
    onGoToAppsScreen: () -> Unit,
    onGoToSearchScreen: () -> Unit,
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
            SettingsScreenIntent.QuickPanelSectionClick -> onGoToQuickPanelScreen()
            SettingsScreenIntent.HomeSectionClick -> onGoToHomeScreen()
            SettingsScreenIntent.AppsSectionClick -> onGoToAppsScreen()
            SettingsScreenIntent.SearchSectionClick -> onGoToSearchScreen()
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
        ContainerColumn(
            contentPadded = true,
            scrollable = true
        ) {
            if (!state.isDefaultLauncher) {
                Box(Modifier.clip(RoundedCornerShape(40.dp))) {
                    SettingsSection(
                        iconId = R.drawable.info,
                        title = "Default Launcher",
                        description = "Click here to set Mordomo as the default launcher",
                        onClick = { onIntent(SettingsScreenIntent.SetDefaultLauncherClick) }
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            Column(Modifier.section()) {
                SettingsSection(
                    iconId = R.drawable.palette,
                    title = "Style",
                    description = "Customize the app theme and icons",
                    onClick = { onIntent(SettingsScreenIntent.StyleSectionClick) }
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(Modifier.section()) {
                SettingsSection(
                    iconId = R.drawable.lightning_outline,
                    title = "Quick Panel",
                    description = "Customize sections and widgets",
                    onClick = { onIntent(SettingsScreenIntent.QuickPanelSectionClick) }
                )

                ListDivider()

                SettingsSection(
                    iconId = R.drawable.home,
                    title = "Home",
                    description = "Customize home buttons and clock",
                    onClick = { onIntent(SettingsScreenIntent.HomeSectionClick) }
                )

                ListDivider()

                SettingsSection(
                    iconId = R.drawable.apps_outline,
                    title = "Apps",
                    description = "Customize apps layout and search",
                    onClick = { onIntent(SettingsScreenIntent.AppsSectionClick) }
                )

                ListDivider()

                SettingsSection(
                    iconId = R.drawable.search,
                    title = "Search",
                    description = "Customize search bar and sections",
                    onClick = { onIntent(SettingsScreenIntent.SearchSectionClick) }
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(
                Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                SettingsSection(
                    iconId = R.drawable.bookmark_outline,
                    title = "Bookmarks",
                    description = "Add bookmarks to quickly open links",
                    onClick = { onIntent(SettingsScreenIntent.BookmarksSectionClick) }
                )

                ListDivider()

                SettingsSection(
                    iconId = R.drawable.globe_outline,
                    title = "Search Engines",
                    description = "Add search engines to search",
                    onClick = { onIntent(SettingsScreenIntent.SearchEnginesSectionClick) }
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(
                Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                SettingsSection(
                    iconId = R.drawable.fingerprint,
                    title = "Security",
                    description = "Hide and secure apps with fingerprint",
                    onClick = { onIntent(SettingsScreenIntent.SecuritySectionClick) }
                )

                ListDivider()

                SettingsSection(
                    iconId = R.drawable.lock_outline,
                    title = "Lock",
                    description = "Manage screen locking gesture",
                    onClick = { onIntent(SettingsScreenIntent.LockSectionClick) }
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(
                Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                SettingsSection(
                    iconId = R.drawable.info,
                    title = "About",
                    description = "Information about this app",
                    onClick = { onIntent(SettingsScreenIntent.AboutSectionClick) }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Preview() {
    SettingsScreen(
        state = SettingsScreenState(
            loading = false,
            isDefaultLauncher = false
        )
    ) {}
}