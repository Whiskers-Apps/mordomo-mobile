package org.whiskersapps.mordomo.mobile.ui.navigation.launcher

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.AppsScreen
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen.AppsScreenRoot
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.HomeScreenRoot
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.search_screen.SearchScreenRoot
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.quick_panel.QuickPanelScreen
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

class LauncherActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val window = this.window
        val view = window.decorView

        fun applyStatusBarColor(useDarkTheme: Boolean) {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !useDarkTheme
        }

        applyStatusBarColor(true)

        setContent {
            MordomoTheme {
                val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)
                val sheetState = rememberStandardBottomSheetState()
                val scaffoldState = rememberBottomSheetScaffoldState(sheetState)
                val darkTheme = isSystemInDarkTheme()

                LaunchedEffect(pagerState.currentPage) {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (pagerState.currentPage != 1) {
                            applyStatusBarColor(darkTheme)
                        } else {
                            applyStatusBarColor(true)
                        }
                    }
                }

                LaunchedEffect(sheetState.currentValue) {
                    if (sheetState.currentValue == SheetValue.Expanded) {
                        applyStatusBarColor(darkTheme)
                    } else {
                        applyStatusBarColor(true)
                    }
                }

                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    containerColor = Color.Transparent,
                    sheetPeekHeight = 0.dp,
                    sheetDragHandle = {},
                    sheetContent = {
                        SearchScreenRoot(sheetState)
                    },
                ) { innerPadding ->
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        state = pagerState,
                    ) { pageIndex ->
                        when (pageIndex) {
                            0 -> {
                                QuickPanelScreen()
                            }

                            1 -> {
                                HomeScreenRoot(pagerState, sheetState)
                            }

                            2 -> {
                                AppsScreenRoot(pagerState)
                            }
                        }
                    }
                }
            }
        }
    }
}