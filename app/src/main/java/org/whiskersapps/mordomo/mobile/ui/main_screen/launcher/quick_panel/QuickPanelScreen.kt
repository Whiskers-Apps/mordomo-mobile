package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.quick_panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun QuickPanelScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Quick Panel com Pinned Apps/Bookmarks e Eventos de Calendario e TODOs",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}