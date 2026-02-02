package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.search_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.search_screen.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenRoot(sheetState: SheetState) {
    SearchScreen()
}

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        SearchBar()
    }
}