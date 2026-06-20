package org.whiskersapps.mordomo.mobile.ui.main_screen.settings.shared_composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SegmentedOption(
    val id: String,
    val text: String
)

@Composable
fun SegmentedSetting(
    title: String,
    description: String,
    activeOptionId: String,
    options: List<SegmentedOption>,
    onChange: (id: String) -> Unit
) {
    Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)) {
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
                color = MaterialTheme.colorScheme.onBackground,
            )
        )

        Spacer(Modifier.height(8.dp))

        MultiChoiceSegmentedButtonRow {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    checked = option.id == activeOptionId,
                    onCheckedChange = { onChange(option.id) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    label = {
                        Text(option.text)
                    },
                    colors = colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        activeBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}