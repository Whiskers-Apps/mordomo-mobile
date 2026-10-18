package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lighttigerxiv.layout_scaffold.inLandscape
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDo
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.composables.WidgetPicker
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.composables.WidgetView
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.bubbleShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.buttonBubble
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState as State

@Composable
fun QuickPanelScreenRoot(
    vm: QuickPanelScreenVM = koinViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    QuickPanelScreen(state) { vm.onAction(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickPanelScreen(
    state: State,
    onIntent: (Intent) -> Unit
) {
    val context = LocalContext.current
    val widgetManager = remember { AppWidgetManager.getInstance(context) }
    val widgetsHost = remember { AppWidgetHost(context, 1) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val inLandscape = inLandscape()
    val splitWidgets by remember(inLandscape) { mutableStateOf(inLandscape) }

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) widgetsHost.startListening()
            if (event == Lifecycle.Event.ON_STOP) widgetsHost.stopListening()
        }

        lifecycle.addObserver(observer)

        onDispose { lifecycle.removeObserver(observer); widgetsHost.stopListening() }
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(state.widgets) {
        for (widget in state.widgets) {
            widgetManager.bindAppWidgetIdIfAllowed(widget.id, widget.info.provider)
        }
    }

    LaunchedEffect(state.widgetPickerState.show) {
        if (state.widgetPickerState.show) {
            scope.launch { scaffoldState.bottomSheetState.expand() }
        } else {
            scope.launch {
                scaffoldState.bottomSheetState.hide()
            }
        }
    }

    LaunchedEffect(sheetState.currentValue) {
        snapshotFlow { sheetState.targetValue }
            .collect { target ->
                if (target != SheetValue.Expanded) onIntent(Intent.WidgetPickerClose)
            }
    }

    if (state.addToDoDialog.show) {
        DialogLayout(onDismiss = { onIntent(AddToDoDialogIntent.Close) }) {
            DialogHeader(R.drawable.plus, "Add To-Do")

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.addToDoDialog.content,
                onValueChange = { onIntent(AddToDoDialogIntent.ContentType(it)) },
                shape = RoundedCornerShape(12.dp),
                label = { Text("To-Do") }
            )

            Spacer(Modifier.height(8.dp))

            DialogFooter(
                onCancel = { onIntent(AddToDoDialogIntent.Close) },
                onPrimaryClick = { onIntent(AddToDoDialogIntent.AddClick) },
                primaryButtonText = "Add"
            )
        }
    }


    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        scaffoldState = scaffoldState,
        sheetContentColor = MaterialTheme.colorScheme.background,
        containerColor = MaterialTheme.colorScheme.background,
        sheetPeekHeight = 0.dp,
        sheetDragHandle = null,
        sheetSwipeEnabled = false,
        sheetMaxWidth = Dp.Unspecified,
        sheetContent = {
            Box(Modifier.systemBarsPadding()) {
                WidgetPicker(
                    widgetsHost = widgetsHost,
                    widgetManager = widgetManager,
                    pickerState = state.widgetPickerState,
                    onIntent = { onIntent(it) }
                )
            }

        },
    ) { scaffoldPadding ->
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .systemBarsPadding()
        ) {
            val width = maxWidth.value
            val cellSize = remember(
                width,
                splitWidgets
            ) { mutableIntStateOf(width.toInt() / if (splitWidgets) 8 else 4) }.intValue

            LazyColumn {
                item {
                    Row(
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODOs",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        Box(
                            Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    onIntent(Intent.AddToDoClick)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    if (state.toDos.isEmpty()) {
                        Row(
                            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.info),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(Modifier.width(4.dp))

                            Text(
                                text = "No TODOs. Click on the add button to create one.",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }


                        Spacer(Modifier.height(8.dp))
                    }
                }

                itemsIndexed(
                    items = state.toDos,
                    key = { _, toDo -> "todo-${toDo.id}" }
                ) { index, toDo ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp)
                            .buttonBubble(
                                index,
                                state.toDos.size,
                            ) { onIntent(Intent.ToDoClick(toDo.id)) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = toDo.content,
                            textDecoration = if (toDo.done) TextDecoration.LineThrough else null,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Checkbox(
                            checked = toDo.done,
                            onCheckedChange = { onIntent(Intent.ToDoClick(toDo.id)) }
                        )

                        Box(
                            Modifier
                                .clip(CircleShape)
                                .clickable { onIntent(Intent.ToDoDeleteClick(toDo.id)) }
                                .padding(8.dp),
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.trash),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    if (index < state.toDos.size - 1)
                        Spacer(Modifier.height(2.dp))
                }

                item {
                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Widgets",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        Box(
                            Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    onIntent(Intent.AddWidgetClick)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }


                if (splitWidgets) {
                    itemsIndexed(
                        items = state.widgets.chunked(2),
                        key = { _, widget -> "${widget.first().id} - ${widget.getOrNull(1)?.id}" }
                    ) { index, row ->
                        val secondWidget = row.getOrNull(1)

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .padding(start = 24.dp, end = 24.dp)
                        ) {
                            WidgetView(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                widget = row.first(),
                                widgetsHost = widgetsHost,
                                cellSize = cellSize,
                                split = true,
                                onIntent = { onIntent(it) }
                            )

                            Spacer(Modifier.width(8.dp))

                            if (secondWidget != null) {
                                WidgetView(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    widget = secondWidget,
                                    widgetsHost = widgetsHost,
                                    cellSize = cellSize,
                                    split = true,
                                    onIntent = { onIntent(it) }
                                )
                            } else {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .weight(1f)
                                )
                            }
                        }

                        if (index < state.widgets.size - 1)
                            Spacer(Modifier.height(8.dp))
                    }
                } else {
                    itemsIndexed(
                        items = state.widgets,
                        key = { _, widget -> widget.id }
                    ) { index, widget ->
                        WidgetView(
                            modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp)
                                .bubbleShape(index, state.widgets.size)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            widget = widget,
                            widgetsHost = widgetsHost,
                            cellSize = cellSize,
                            split = false,
                            onIntent = { onIntent(it) }
                        )

                        if (index < state.widgets.size - 1)
                            Spacer(Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun QuickPanelScreenPreview() {
    val state = State(
        loading = false,
        toDos = listOf(
            ToDo(
                id = 1,
                content = "Buy Groceries",
                done = false
            ),
            ToDo(
                id = 2,
                content = "Take the trash out",
                done = true
            )
        ),
        quickApps = listOf(),
        addToDoDialog = State.AddToDoDialogState(
            show = true,
            content = "Buy gro"
        )
    )

    QuickPanelScreen(state) {}
}