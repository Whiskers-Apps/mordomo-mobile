package org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TOP_SHAPE

@Composable
fun SwitchSetting(
    shape: Shape,
    title: String,
    description: String,
    value: Boolean,
    onValueChange: (value: Boolean) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onValueChange(!value) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.width(8.dp))

        Switch(
            checked = value,
            onCheckedChange = { onValueChange(it) }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SwitchSettingPreview() {
    var value by remember { mutableStateOf(true) }

    SwitchSetting(
        shape = TOP_SHAPE,
        title = "Black",
        description = "Override dark theme with a black background",
        value = value,
        onValueChange = { value = it }
    )
}