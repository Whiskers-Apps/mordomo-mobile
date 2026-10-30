package org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen

import android.appwidget.AppWidgetManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import org.koin.compose.viewmodel.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.settings.AppSettings
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen.QuickPanelSettingsScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen.QuickPanelSettingsScreenState as ScreenState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen.QuickPanelSettingsScreenUICallback as UICallback
import org.whiskersapps.mordomo.mobile.ui.screens.settings.quick_panel_screen.QuickPanelSettingsScreenVM as ScreenVM

@Composable
fun QuickPanelSettingsScreenRoot(
    onBack: () -> Unit,
    vm: ScreenVM = koinViewModel()
) {
    LaunchedEffect(Unit) {
        vm.uiCallback.collect { uiCallback ->
            when (uiCallback) {
                UICallback.Back -> onBack()
            }
        }
    }

    QuickPanelSettingsScreen(
        state = vm.state.collectAsState().value,
        onIntent = { vm.onIntent(it) }
    )
}

@Composable
fun QuickPanelSettingsScreen(
    state: ScreenState,
    onIntent: (ScreenIntent) -> Unit
) {
    val listState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(listState) { from, to ->
        onIntent(ScreenIntent.SectionReorder(from.index, to.index))
    }

    val widgetsListState = rememberLazyListState()
    val reorderableLazyWidgetsListState =
        rememberReorderableLazyListState(widgetsListState) { from, to ->
            onIntent(ScreenIntent.WidgetReorder(from.index, to.index))
        }

    val context = LocalContext.current
    val widgetManager = remember { AppWidgetManager.getInstance(context) }

    if (!state.loading) {
        ContainerColumn {
            Nav("Quick Panel") { onIntent(ScreenIntent.Back) }

            BoxWithConstraints(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {
                val height = this.maxHeight
                val width = this.maxWidth

                Column(
                    Modifier
                        .height(height)
                        .width(width)
                ) {
                    LazyColumn(
                        modifier = Modifier.section(),
                        state = listState
                    ) {
                        itemsIndexed(
                            state.sections,
                            key = { _, section -> section.id }
                        ) { index, section ->
                            ReorderableItem(
                                reorderableLazyListState,
                                key = section.id
                            ) { isDragging ->
                                Column {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .background(if (isDragging) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            text = when (section.id) {
                                                SettingsValues.QuickSections.APPS -> "Apps"
                                                SettingsValues.QuickSections.BOOKMARKS -> "Bookmarks"
                                                SettingsValues.QuickSections.WIDGETS -> "Widgets"
                                                else -> "TODOs"
                                            },
                                            color = MaterialTheme.colorScheme.onBackground,
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Spacer(Modifier.width(16.dp))

                                        Box(
                                            Modifier
                                                .size(48.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.background)
                                                .clickable {
                                                    onIntent(
                                                        ScreenIntent.HideSectionClick(
                                                            section.id,
                                                            !section.hidden
                                                        )
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .size(24.dp),
                                                painter = painterResource(if (section.hidden) R.drawable.eye_closed else R.drawable.eye),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onBackground
                                            )
                                        }

                                        Box(Modifier.minimumInteractiveComponentSize()) {
                                            Icon(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .draggableHandle(
                                                        onDragStopped = { onIntent(ScreenIntent.SectionDragStop) }
                                                    ),
                                                painter = painterResource(R.drawable.drag),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }

                                    if (index < state.sections.size - 1) {
                                        ListDivider()
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.section(),
                        state = widgetsListState
                    ) {
                        itemsIndexed(
                            state.widgets,
                            key = { _, widget -> widget.name + widget.id }
                        ) { index, widget ->
                            val info by remember(widget.name) {
                                mutableStateOf(widgetManager.getAppWidgetInfo(widget.id))
                            }

                            val name: String? = info?.loadLabel(context.packageManager)

                            val appIcon: ImageBitmap? = info?.let {
                                runCatching { context.packageManager.getApplicationIcon(it.provider.packageName) }.getOrNull()
                                    ?.toBitmap()?.asImageBitmap()
                            }

                            ReorderableItem(
                                reorderableLazyWidgetsListState,
                                key = widget.name + widget.id
                            ) { isDragging ->
                                Column {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .background(if (isDragging) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        appIcon?.let {
                                            Image(
                                                modifier = Modifier.size(40.dp),
                                                bitmap = appIcon,
                                                contentDescription = null
                                            )

                                            Spacer(Modifier.width(8.dp))
                                        }

                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            text = name ?: "ERROR",
                                            color = MaterialTheme.colorScheme.onBackground,
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Box(Modifier.minimumInteractiveComponentSize()) {
                                            Icon(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .draggableHandle(
                                                        onDragStopped = { onIntent(ScreenIntent.SectionDragStop) }
                                                    ),
                                                painter = painterResource(R.drawable.drag),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }

                                    if (index < state.widgets.size - 1) {
                                        ListDivider()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewQuickPanelSettingsScreen() {
    MordomoTheme {
        QuickPanelSettingsScreen(
            ScreenState(
                loading = false,
                sections = AppSettings.QuickPanel.DEFAULT_SECTIONS
            )
        ) { }
    }
}