package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App

@Composable
fun AppDialog(
    app: App,
    onDismiss: () -> Unit,
    onAppInfoClick: (app: App) -> Unit,
    onUninstallClick: (app: App) -> Unit,
    onShortcutClick: (shortcut: App.Shortcut) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .padding(24.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SingleCardShape)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppIcon(themed = false, size = 64.dp, icons = app.icons)

                Spacer(Modifier.height(8.dp))

                Text(text = app.name, color = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SingleCardShape)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .clip(LeftCardShape)
                        .background(MaterialTheme.colorScheme.background)
                        .clickable { onAppInfoClick(app) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.info),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.width(8.dp))

                    Text("App Info", color = MaterialTheme.colorScheme.onBackground)
                }

                Spacer(Modifier.width(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .clip(RightCardShape)
                        .background(MaterialTheme.colorScheme.background)
                        .clickable { onUninstallClick(app) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.trash),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.width(8.dp))

                    Text("Uninstall", color = MaterialTheme.colorScheme.onBackground)
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                itemsIndexed(
                    app.shortcuts,
                    key = { _, shortcut -> shortcut.text }
                ) { index, shortcut ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                            .clip(getShapeByIndex(index, app.shortcuts.size))
                            .background(MaterialTheme.colorScheme.background)
                            .clickable { onShortcutClick(shortcut) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        shortcut.icon?.let { icon ->
                            Image(
                                modifier = Modifier.size(24.dp),
                                bitmap = remember { derivedStateOf { icon.asImageBitmap() } }.value,
                                contentDescription = null,
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = shortcut.text,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}