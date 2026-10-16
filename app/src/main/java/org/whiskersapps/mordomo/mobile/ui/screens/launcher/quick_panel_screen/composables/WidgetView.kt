package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.composables

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetProviderInfo
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.carousel.Arrangement
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.widgets.Widget
import org.whiskersapps.mordomo.mobile.ui.shared_composables.conditionalModifier
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.QuickPanelScreenIntent as Intent

@Composable
fun WidgetView(
    modifier: Modifier,
    widget: Widget,
    widgetsHost: AppWidgetHost,
    cellSize: Int,
    onIntent: (Intent) -> Unit
) {
    val context = LocalContext.current

    var cols by remember { mutableIntStateOf(widget.cols) }
    var rows by remember { mutableIntStateOf(widget.rows) }

    val widgetWidth = cellSize * cols
    val widgetHeight = cellSize * rows

    var showEdit by remember { mutableStateOf(false) }
    var preEditRows by remember { mutableIntStateOf(rows) }
    var preEditCols by remember { mutableIntStateOf(cols) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    preEditRows = rows
                    preEditCols = cols

                    showEdit = true
                }
            )
            .padding(8.dp)
    ) {
        if (showEdit) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(8.dp)
            ) {
                Column {
                    Box(
                        Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    bottomStart = 8.dp,
                                    topEnd = 2.dp,
                                    bottomEnd = 2.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.width),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Box(
                        Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    bottomStart = 8.dp,
                                    topEnd = 2.dp,
                                    bottomEnd = 2.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.height),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(Modifier.width(4.dp))

                Column {
                    Row(
                        Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 2.dp,
                                    bottomStart = 2.dp,
                                    topEnd = 8.dp,
                                    bottomEnd = 8.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.background),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .clickable {
                                    cols = (cols + 1).coerceAtMost(4)
                                }
                                .padding(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Box(
                            Modifier
                                .clip(CircleShape)
                                .clickable {
                                    cols = (cols - 1).coerceAtLeast(1)
                                }
                                .padding(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.minus),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(
                        Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 2.dp,
                                    bottomStart = 2.dp,
                                    topEnd = 8.dp,
                                    bottomEnd = 8.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.background),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .clickable {
                                    rows = (rows + 1).coerceAtMost(7)
                                }
                                .padding(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Box(
                            Modifier
                                .clip(CircleShape)
                                .clickable {
                                    rows = (rows - 1).coerceAtLeast(1)
                                }
                                .padding(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.minus),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                Column() {
                    Box(
                        Modifier
                            .width(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                            .clickable {
                                rows = preEditRows
                                cols = preEditCols

                                showEdit = false
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.cross),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Box(
                        Modifier
                            .width(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                            .clickable {
                                showEdit = false
                                onIntent(Intent.DeleteWidgetClick(widget))
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.trash),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onError
                        )
                    }
                }

                Spacer(Modifier.width(4.dp))

                Column(
                ) {
                    Box(
                        Modifier
                            .width(80.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .clickable {
                                showEdit = false

                                onIntent(Intent.SaveWidgetClick(widget.id, rows, cols))
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.check),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        Column(
            Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                modifier = Modifier
                    .height(widgetHeight.dp)
                    .width(widgetWidth.dp)
                    .conditionalModifier(showEdit) {
                        border(
                            2.dp,
                            MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(16.dp)
                        )
                    },
                factory = {
                    widgetsHost.createView(
                        context.applicationContext,
                        widget.id,
                        widget.info
                    )
                },
                update = { view ->
                    view.updateAppWidgetSize(
                        null,
                        widgetWidth,
                        widgetHeight,
                        widgetWidth,
                        widgetHeight
                    )
                }
            )
        }
    }
}