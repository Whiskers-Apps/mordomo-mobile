package org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TextInput

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
        DialogLayout(
            onDismiss = { onCancel() }
        ) {
            DialogHeader(state.title)

            TextInput(
                value = state.value,
                onValueChange = { onValueChange(it) },
                label = state.label
            )

            Spacer(Modifier.height(8.dp))

            DialogFooter(
                primaryButtonText = "Save",
                onPrimaryClick = { onSave(state.value) },
                onCancel = { onCancel() }
            )
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