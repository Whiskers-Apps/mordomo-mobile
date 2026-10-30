package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lighttigerxiv.layout_scaffold.inLandscape
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDo
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.composables.AppRow
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.composables.WidgetPicker
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.composables.WidgetView
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables.AppsPicker
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.InfoCard
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TextInput
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenVM as ScreenVM

@Composable
fun QuickPanelScreenRoot(
    onGoHome: () -> Unit,
    vm: ScreenVM = koinViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        vm.screenCallback.collect {
            when (it) {
                QuickPanelScreenCallback.GoHome -> onGoHome()
            }
        }
    }

    QuickPanelScreen(state) { vm.onAction(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickPanelScreen(
    state: State,
    onIntent: (ScreenIntent) -> Unit
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

    LaunchedEffect(
        state.widgetPickerState.show,
        state.appsPickerState.show,
        state.bookmarksPickerState.show
    ) {
        scope.launch {
            val show = state.widgetPickerState.show
                    || state.appsPickerState.show
                    || state.bookmarksPickerState.show

            if (show) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.hide()
            }
        }
    }

    LaunchedEffect(sheetState.currentValue) {
        snapshotFlow { sheetState.currentValue }
            .collect { target ->
                if (target == SheetValue.PartiallyExpanded) {
                    onIntent(ScreenIntent.WidgetPickerClose)
                    onIntent(ScreenIntent.AppsPickerClose)
                    onIntent(ScreenIntent.BookmarksPickerClose)
                }
            }
    }

    if (state.addToDoDialog.show) {
        DialogLayout(onDismiss = { onIntent(AddToDoDialogIntent.Close) }) {
            DialogHeader("Add To-Do")

            TextInput(
                value = state.addToDoDialog.content,
                onValueChange = { onIntent(AddToDoDialogIntent.ContentType(it)) },
                label = "To-Do",
                errorMessage = state.addToDoDialog.contentError
            )

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
        sheetContent = {
            if (state.widgetPickerState.show) {
                WidgetPicker(
                    widgetsHost = widgetsHost,
                    widgetManager = widgetManager,
                    pickerState = state.widgetPickerState,
                    onIntent = { onIntent(it) }
                )
            }

            if (state.appsPickerState.show) {
                AppsPicker(
                    state = state.appsPickerState.pickerState,
                    title = "Quick Apps",
                    selectedIconId = R.drawable.star,
                    onClose = { onIntent(ScreenIntent.AppsPickerClose) },
                    onAppClick = { onIntent(ScreenIntent.AppsPickerSelect(it.packageName)) },
                    onSaveClick = { onIntent(ScreenIntent.AppsPickerSave) },
                )
            }
        },
    ) { scaffoldPadding ->
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            val width = maxWidth.value
            val cellSize = remember(
                width,
                splitWidgets
            ) { mutableIntStateOf(width.toInt() / if (splitWidgets) 8 else 4) }.intValue

            Column(Modifier.verticalScroll(rememberScrollState())) {
                for (section in state.sections) {
                    if (!section.hidden) {
                        when (section.id) {
                            SettingsValues.QuickSections.APPS -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Apps",
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
                                                onIntent(ScreenIntent.AddAppClick)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(R.drawable.pencil),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))

                                if (state.quickApps.isEmpty()) {
                                    Box(Modifier.section()) {
                                        InfoCard(
                                            iconId = R.drawable.info,
                                            title = "No Apps",
                                            description = "There are no available apps. Add them in settings."
                                        )
                                    }
                                }

                                Column(Modifier.section()) {
                                    for ((index, app) in state.quickApps.withIndex()) {
                                        key(app.packageName) {
                                            AppRow(
                                                app,
                                                padding = PaddingValues(16.dp),
                                                size = 40.dp,
                                                onClick = { onIntent(ScreenIntent.AppClick(app)) },
                                                onLongClick = {},
                                            )

                                            if (index < state.quickApps.size - 1) {
                                                ListDivider()
                                            }
                                        }
                                    }
                                }

                                Spacer(Modifier.height(8.dp))
                            }

                            SettingsValues.QuickSections.BOOKMARKS -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Bookmarks",
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
                                                onIntent(ScreenIntent.AddBookmarkClick)
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

                                if (state.quickBookmarks.isEmpty()) {
                                    Box(Modifier.section()) {
                                        InfoCard(
                                            iconId = R.drawable.info,
                                            title = "No Bookmarks",
                                            description = "There are no available bookmarks. Add them in settings."
                                        )
                                    }

                                    Spacer(Modifier.height(8.dp))
                                }
                            }

                            SettingsValues.QuickSections.WIDGETS -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
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
                                                onIntent(ScreenIntent.AddWidgetClick)
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

                                if (state.widgets.isEmpty()) {
                                    Box(Modifier.section()) {
                                        InfoCard(
                                            iconId = R.drawable.info,
                                            title = "No Widgets",
                                            description = "There are no available widgets. Click on the add button to create one."
                                        )
                                    }

                                    Spacer(Modifier.height(8.dp))
                                }

                                Column(Modifier.section()) {
                                    if (splitWidgets) {
                                        state.widgets.chunked(2).forEachIndexed { index, row ->
                                            val secondWidget = row.getOrNull(1)

                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(IntrinsicSize.Min)
                                            ) {
                                                key("widget-${row.first().id}") {
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
                                                }


                                                Spacer(Modifier.width(8.dp))

                                                if (secondWidget != null) {

                                                    key("widget-${secondWidget.id}") {
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
                                                    }
                                                } else {
                                                    Box(
                                                        Modifier
                                                            .fillMaxSize()
                                                            .weight(1f)
                                                    )
                                                }
                                            }

                                            if (index < state.widgets.size - 1) {
                                                ListDivider()
                                            }
                                        }
                                    } else {
                                        state.widgets.forEachIndexed { index, widget ->
                                            key("widget-${widget.id}") {
                                                WidgetView(
                                                    modifier = Modifier,
                                                    widget = widget,
                                                    widgetsHost = widgetsHost,
                                                    cellSize = cellSize,
                                                    split = false,
                                                    onIntent = { onIntent(it) }
                                                )
                                            }

                                            if (index < state.widgets.size - 1) {
                                                ListDivider()
                                            }
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))
                            }

                            SettingsValues.QuickSections.TODOS -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
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
                                                onIntent(ScreenIntent.AddToDoClick)
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
                                    Box(Modifier.section()) {
                                        InfoCard(
                                            iconId = R.drawable.info,
                                            title = "TODOs",
                                            description = "There are no available TODOs. Click on the add button to create one."
                                        )
                                    }

                                    Spacer(Modifier.height(8.dp))
                                }

                                Column(Modifier.section()) {
                                    state.toDos.forEachIndexed { index, toDo ->
                                        key("todo-${toDo.id}") {
                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        onIntent(
                                                            ScreenIntent.ToDoClick(
                                                                toDo.id
                                                            )
                                                        )
                                                    }
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = toDo.done,
                                                    onCheckedChange = {
                                                        onIntent(
                                                            ScreenIntent.ToDoClick(
                                                                toDo.id
                                                            )
                                                        )
                                                    }
                                                )

                                                Spacer(Modifier.width(8.dp))

                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f),
                                                    text = toDo.content,
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        textDecoration = if (toDo.done) TextDecoration.LineThrough else null
                                                    )
                                                )

                                                Spacer(Modifier.width(8.dp))

                                                Box(
                                                    Modifier
                                                        .size(48.dp)
                                                        .clip(CircleShape)
                                                        .background(MaterialTheme.colorScheme.background)
                                                        .clickable {
                                                            onIntent(
                                                                ScreenIntent.ToDoDeleteClick(
                                                                    toDo.id
                                                                )
                                                            )
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        modifier = Modifier.size(24.dp),
                                                        painter = painterResource(R.drawable.trash),
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            }

                                            if (index < state.toDos.size - 1) {
                                                ListDivider()
                                            }
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
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
            show = false,
            content = "Buy gro"
        )
    )

    QuickPanelScreen(state) {}
}