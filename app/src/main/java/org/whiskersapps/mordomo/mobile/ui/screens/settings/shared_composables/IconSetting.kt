package org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.shared_composables.MIDDLE_SHAPE
import org.whiskersapps.mordomo.mobile.ui.shared_composables.bubble
import org.whiskersapps.mordomo.mobile.ui.shared_composables.buttonBubble

@Composable
fun IconSetting(
    iconId: Int,
    title: String,
    description: String,
    shape: RoundedCornerShape,
    onClick: () -> Unit = {}
) {
    Row(
        Modifier
            .buttonBubble(
                0, 1,
            ) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(iconId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Text(
                text = description,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

@Composable
@Preview
fun IconSettingPreview() {
    IconSetting(
        iconId = R.drawable.info,
        title = "Hidden Apps",
        description = "Select apps that should be hidden",
        shape = MIDDLE_SHAPE
    )
}