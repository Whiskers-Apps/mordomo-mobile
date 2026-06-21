package org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwitchSetting(
    title: String,
    description: String,
    value: Boolean,
    onValueChange: (value: Boolean) -> Unit,
    padding: PaddingValues = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onValueChange(!value) }
            .padding(padding),
        verticalAlignment = Alignment.Top
    ) {
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

        Spacer(Modifier.width(16.dp))

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
        title = "Black",
        description = "Override dark theme with a black background",
        value = value,
        onValueChange = { value = it }
    )
}