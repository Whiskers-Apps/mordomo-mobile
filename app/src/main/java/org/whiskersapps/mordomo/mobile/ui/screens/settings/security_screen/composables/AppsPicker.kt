package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenState.Picker
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader


@Composable
fun AppsPicker(
    picker: Picker,
    title: String,
    selectedIconId: Int,
    onClose: () -> Unit,
    onAppClick: (app: App) -> Unit,
    onSaveClick: () -> Unit
) {

    DialogLayout(onDismiss = { onClose() }) {
        DialogHeader(R.drawable.apps_outline,title)

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, fill = true),
        ) {
            items(items = picker.apps, key = { it.packageName }) { app ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable() {
                            onAppClick(app)
                        }
                        .padding(top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        themed = false,
                        size = 48.dp,
                        icons = app.icons,
                        shape = CircleShape,
                    )

                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = app.name,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                    )

                    if (app.packageName in picker.selectedApps) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(selectedIconId),
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = null
                            )
                        }

                    }
                }
            }
        }

        DialogFooter(
            primaryButtonText = "Save",
            onCancel = { onClose() },
            onPrimaryClick = { onSaveClick() }
        )
    }
}