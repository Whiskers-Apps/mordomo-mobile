package org.whiskersapps.mordomo.mobile.ui.screens.launcher.apps_screen.composables

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppIcon

@Composable
fun AppRow(
    app: App,
    shape: RoundedCornerShape = CircleShape,
    padding: PaddingValues = PaddingValues(8.dp),
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .combinedClickable(
                onClick = {
                    onClick()
                },
                onLongClick = {
                    onLongClick()
                }
            )
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppIcon(themed = false, size = 48.dp, icons = app.icons)

        Spacer(Modifier.width(8.dp))

        Text(app.name, color = MaterialTheme.colorScheme.onBackground, fontSize = 13.sp)
    }
}