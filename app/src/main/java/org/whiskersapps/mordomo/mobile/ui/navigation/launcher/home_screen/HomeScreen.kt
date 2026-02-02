package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    pagerState: PagerState,
    sheetState: SheetState
) {
    val scope = rememberCoroutineScope()

    HomeScreen { action ->
        when (action) {
            is HomeScreenAction.AppsButtonClick -> {
                scope.launch {
                    pagerState.scrollToPage(2)
                }
            }

            HomeScreenAction.SearchButtonClick -> {
                scope.launch {
                    sheetState.expand()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onAction: (HomeScreenAction) -> Unit) {
    val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val statusBarSize = remember(statusBarInset) { statusBarInset + 16.dp }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarSize)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .padding(24.dp)
                .systemBarsPadding()
        ) {
            Spacer(
                Modifier
                    .fillMaxHeight()
                    .weight(1f, fill = true)
            )

            Row(Modifier.fillMaxWidth()) {
                OutlinedButton(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .weight(1f, fill = true),
                    onClick = {
                        onAction(HomeScreenAction.SearchButtonClick)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    shape = RoundedCornerShape(
                        topStart = 99.dp,
                        bottomStart = 99.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.search),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(modifier = Modifier.fillMaxWidth(), text = "Search")
                }

                Spacer(Modifier.width(4.dp))

                OutlinedButton(
                    modifier = Modifier
                        .height(56.dp)
                        .wrapContentWidth(),
                    onClick = {
                        onAction(HomeScreenAction.AppsButtonClick)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = 99.dp,
                        bottomEnd = 99.dp
                    )
                ) {

                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.apps),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(text = "Apps")
                }
            }
        }
    }
}