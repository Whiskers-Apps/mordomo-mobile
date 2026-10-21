package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.composables

import android.app.Activity
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.R.drawable
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.InfoMessage
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.bubble
import org.whiskersapps.mordomo.mobile.ui.shared_composables.buttonBubble
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import kotlin.math.max
import android.content.Intent as AndroidIntent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenState.WidgetPickerState as State

@Composable
fun WidgetPicker(
    widgetsHost: AppWidgetHost,
    widgetManager: AppWidgetManager,
    pickerState: State,
    onIntent: (Intent) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var pendingWidget by remember { mutableStateOf<AppWidgetProviderInfo?>(null) }
    var pendingWidgetId by remember { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current


    BackHandler {
        focusManager.clearFocus()
        onIntent(Intent.WidgetPickerClose)
    }

    fun bindWidget() {
        try {
            if (pendingWidget!!.configure != null) {
                widgetsHost.startAppWidgetConfigureActivityForResult(
                    activity ?: throw Exception("Failed to get activity"),
                    pendingWidgetId,
                    0,
                    1,
                    null
                )
            }

            onIntent(Intent.WidgetBound(pendingWidgetId, pendingWidget!!.provider.className))
        } catch (e: Exception) {
            widgetsHost.deleteAppWidgetId(pendingWidgetId)
            Log.e("Widget Picker", e.toString())
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { r ->
        if (r.resultCode == Activity.RESULT_OK)
            bindWidget()
        else
            widgetsHost.deleteAppWidgetId(pendingWidgetId)
    }

    fun attemptWidgetBinding(info: AppWidgetProviderInfo) {
        try {
            pendingWidget = info
            pendingWidgetId = widgetsHost.allocateAppWidgetId()

            val allowed = widgetManager.bindAppWidgetIdIfAllowed(pendingWidgetId, info.provider)

            if (allowed) {
                bindWidget()
                return
            }

            launcher.launch(AndroidIntent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, pendingWidgetId)
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, info.provider)
            })
        } catch (e: Exception) {
            Log.d("Widget Picker", "Failed to attempt binding", e)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .widthIn(max = 900.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {onIntent(Intent.WidgetPickerClose)},
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(Modifier.width(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = "Pick a Widget",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.width(48.dp))
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .section(),
            ) {
                item {
                    if (pickerState.apps.isEmpty()) {
                        InfoMessage("No apps matching the search query.")
                    }
                }

                itemsIndexed(
                    items = pickerState.apps,
                    key = { _, app -> app.packageName }
                ) { index, app ->
                    var opened by remember(pickerState.openedWidgets) {
                        mutableStateOf(pickerState.openedWidgets.contains(app.packageName))
                    }

                    Column(
                        modifier = Modifier
                            .clickable {
                                onIntent(Intent.AppWidgetCardClick(app.packageName))
                            }
                            .padding(16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppIcon(
                                themed = false,
                                size = 40.dp,
                                icons = app.icons,
                                shape = CircleShape,
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                text = app.name,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.background),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(if (opened) R.drawable.chevron_up else R.drawable.chevron_down),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        if (opened) {
                            Spacer(Modifier.height(16.dp))

                            for (info in app.widgets) {
                                key("${app.packageName} - ${info.provider.className}") {
                                    val name = info.loadLabel(context.packageManager)

                                    val preview = info.loadPreviewImage(context, 0)?.toSafeBitmap()
                                        ?: info.loadIcon(context, 0)?.toSafeBitmap()

                                    Column(
                                        Modifier
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                onIntent(Intent.WidgetClick)
                                                attemptWidgetBinding(info)
                                            }
                                            .padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (preview != null) {
                                            Image(
                                                modifier = Modifier
                                                    .widthIn(max = 200.dp)
                                                    .fillMaxWidth(),
                                                bitmap = preview,
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit
                                            )
                                        }

                                        Spacer(Modifier.height(8.dp))

                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            text = name,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                    }
                                }
                            }
                        }
                    }

                    if (index < pickerState.apps.size - 1) {
                        ListDivider()
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = pickerState.searchText,
                onValueChange = { onIntent(Intent.SearchWidgetType(it)) },
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(drawable.search),
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    if (pickerState.searchText.isEmpty()) {
                        Box(Modifier.size(24.dp))
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onIntent(Intent.SearchWidgetType("")) },
                            painter = painterResource(R.drawable.clear_circle),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = "Search Apps",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true
            )
        }
    }
}

// Some previews have 0 width or height
@Composable
fun Drawable.toSafeBitmap(fallbackPx: Int = 256): ImageBitmap? = runCatching {
    val w = intrinsicWidth.takeIf { it > 0 } ?: fallbackPx
    val h = intrinsicHeight.takeIf { it > 0 } ?: fallbackPx
    toBitmap(w, h).asImageBitmap()
}.getOrNull()

@Composable
@Preview
fun PreviewWidgetPicker() {
    val context = LocalContext.current
    val widgetManager = remember { AppWidgetManager.getInstance(context) }
    val widgetsHost = remember { AppWidgetHost(context, 1) }

    WidgetPicker(
        widgetsHost = widgetsHost,
        widgetManager = widgetManager,
        pickerState = State(
            show = true,
            apps = emptyList(),
            searchText = "lorem ipsum"
        ),
        onIntent = { },
    )
}