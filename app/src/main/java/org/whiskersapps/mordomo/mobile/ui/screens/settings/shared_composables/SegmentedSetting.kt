package org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.ui.shared_composables.MIDDLE_SHAPE

data class SegmentedOption(
    val id: String,
    val text: String
)

@Composable
fun SegmentedSetting(
    shape: Shape,
    title: String,
    description: String,
    activeOptionId: String,
    options: List<SegmentedOption>,
    onChange: (id: String) -> Unit
) {
    Column(
        Modifier.clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
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

        Spacer(Modifier.height(8.dp))

        MultiChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    checked = option.id == activeOptionId,
                    onCheckedChange = { onChange(option.id) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    label = {
                        Text(
                            text = option.text,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    colors = colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        activeBorderColor = MaterialTheme.colorScheme.onBackground,
                        inactiveBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewSegmentSetting() {
    SegmentedSetting(
        shape = MIDDLE_SHAPE,
        title = "Clock Position",
        description = "Clock position in the home screen",
        options = listOf(
            SegmentedOption(
                id = SettingsValues.ClockPosition.TOP,
                text = "Top"
            ),
            SegmentedOption(
                id = SettingsValues.ClockPosition.CENTER,
                text = "Center"
            )
        ),
        activeOptionId = SettingsValues.ClockPosition.TOP,
        onChange = {}
    )

}