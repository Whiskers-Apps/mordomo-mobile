package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.apps_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.ui.composables.AppIcon

@Composable
fun AppsScreenRoot(
    pagerState: PagerState,
    vm: AppsScreenVM = koinViewModel()
) {
    val scope = rememberCoroutineScope()

    AppsScreen(vm.state.collectAsState().value) { action ->
        when (action) {
            is AppsScreenAction.AppClick -> {
                vm.onAction(action)

                scope.launch {
                    pagerState.scrollToPage(1)
                }
            }

            else -> {
                vm.onAction(action)
            }
        }
    }
}

@Composable
fun AppsScreen(state: AppsScreenState, onAction: (AppsScreenAction) -> Unit) {
    if (!state.loading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(state.apps, key = { it.packageName }) { app ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .clickable {
                                onAction(AppsScreenAction.AppClick(app))
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppIcon(themed = false, size = 48.dp, icons = app.icons)

                        Spacer(Modifier.width(8.dp))

                        Text(app.name, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
        }
    }

}