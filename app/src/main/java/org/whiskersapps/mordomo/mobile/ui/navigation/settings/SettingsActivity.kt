package org.whiskersapps.mordomo.mobile.ui.navigation.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.whiskersapps.mordomo.mobile.core.features.routes.Routes
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.home_screen.HomeSettingsScreen
import org.whiskersapps.mordomo.mobile.ui.navigation.settings.home_screen.HomeSettingsScreenRoot
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MordomoTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.SETTINGS_ROOT
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

                    composable(Routes.SETTINGS_APPS) { }

                    composable(Routes.SETTINGS_BOOKMARKS) { }

                    composable(Routes.SETTINGS_SEARCH_ENGINES) { }

                    composable(Routes.SETTINGS_SECURITY) { }

                    composable(Routes.SETTINGS_LOCK) { }

                    composable(Routes.SETTINGS_ABOUT) { }
                }
            }
        }
    }
}