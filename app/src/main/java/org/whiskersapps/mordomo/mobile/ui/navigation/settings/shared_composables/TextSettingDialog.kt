package org.whiskersapps.mordomo.mobile.ui.navigation.settings.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.whiskersapps.mordomo.mobile.ui.shared_composables.RightCardShape

data class TextSettingDialogState(
    val show: Boolean = false,
    val title: String = "",
    val label: String = "",
    val value: String = "",
    val saveEnabled: Boolean = true
)

@Composable
fun TextSettingDialog(
    state: TextSettingDialogState,
    onValueChange: (text: String) -> Unit,
    onCancel: () -> Unit,
    onSave: (value: String) -> Unit
) {
    if (state.show) {
        Dialog(
            onDismissRequest = { onCancel() },
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = state.title,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.value,
                        onValueChange = { onValueChange(it) },
                        shape = RoundedCornerShape(8.dp),
                        label = {
                            Text(state.label)
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { onCancel() }
                        ) {
                            Text("Cancel")
                        }

                        Spacer(Modifier.width(8.dp))

                        Button(onClick = { onSave(state.value) }, enabled = state.saveEnabled) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    TextSettingDialog(
        TextSettingDialogState(
            show = true,
            title = "Date Format",
            label = "Format",
            saveEnabled = true
        ),
        onValueChange = {},
        onCancel = {},
        onSave = {}
    )
}