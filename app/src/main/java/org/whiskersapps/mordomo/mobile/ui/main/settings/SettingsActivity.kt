package org.whiskersapps.mordomo.mobile.ui.main.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.whiskersapps.mordomo.mobile.core.features.routes.Routes
import org.whiskersapps.mordomo.mobile.ui.main.settings.apps_screen.AppsSettingsScreenRoot
import org.whiskersapps.mordomo.mobile.ui.main.settings.bookmarks_screen.BookmarksScreenRoot
import org.whiskersapps.mordomo.mobile.ui.main.settings.home_screen.HomeSettingsScreenRoot
import org.whiskersapps.mordomo.mobile.ui.main.settings.search_engines_screen.SearchEnginesScreen
import org.whiskersapps.mordomo.mobile.ui.main.settings.search_engines_screen.SearchEnginesScreenRoot
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MordomoTheme {
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    navController = navController,
                    startDestination = Routes.SETTINGS_ROOT,
                ) {
                    composable(Routes.SETTINGS_ROOT) {
                        SettingsScreenRoot(
                            onGoToStyleScreen = { navController.navigate(Routes.SETTINGS_STYLE) },
                            onGoToHomeScreen = { navController.navigate(Routes.SETTINGS_HOME) },
                            onGoToAppsScreen = { navController.navigate(Routes.SETTINGS_APPS) },
                            onGoToBookmarksScreen = { navController.navigate(Routes.SETTINGS_BOOKMARKS) },
                            onGoToSearchEnginesScreen = { navController.navigate(Routes.SETTINGS_SEARCH_ENGINES) },
                            onGoToSecurityScreen = { navController.navigate(Routes.SETTINGS_SECURITY) },
                            onGoToLockScreen = { navController.navigate(Routes.SETTINGS_LOCK) },
                            onGoToAboutScreen = { navController.navigate(Routes.SETTINGS_ABOUT) },
                        )
                    }

                    composable(Routes.SETTINGS_STYLE) { }

                    composable(Routes.SETTINGS_HOME) {
                        HomeSettingsScreenRoot(onBackClick = { navController.navigateUp() })
                    }

                    composable(Routes.SETTINGS_APPS) {
                        AppsSettingsScreenRoot(
                            onBackClick = { navController.navigateUp() }
                        )
                    }

                    composable(Routes.SETTINGS_BOOKMARKS) {
                        BookmarksScreenRoot(
                            onBackClick = { navController.navigateUp() }
                        )
                    }

                    composable(Routes.SETTINGS_SEARCH_ENGINES) {
                        SearchEnginesScreenRoot(
                            onBackClick = {navController.navigateUp()}
                        )
                    }

                    composable(Routes.SETTINGS_SECURITY) { }

                    composable(Routes.SETTINGS_LOCK) { }

                    composable(Routes.SETTINGS_ABOUT) { }
                }
            }
        }
    }
}