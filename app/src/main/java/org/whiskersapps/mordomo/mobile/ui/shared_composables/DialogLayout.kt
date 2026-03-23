package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.whiskersapps.mordomo.mobile.R


@Composable
fun DialogTitle(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
fun DialogFooter(
    primaryButtonText: String = "",
    onCancel: () -> Unit,
    enabled: Boolean = true,
    onPrimaryClick: () -> Unit = {},
) {
    Column {
        Row {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    modifier = Modifier.conditionalModifier(primaryButtonText.isEmpty()) {
                        this.fillMaxWidth()
                    },
                    onClick = { onCancel() },
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                ) {
                    Text(text = stringResource(R.string.Cancel))
                }

                if (primaryButtonText.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = { onPrimaryClick() }, enabled = enabled) {
                        Text(text = primaryButtonText)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DialogTitlePreview() {
    DialogTitle(
        title = "Select Category"
    )
}

@Preview(showBackground = true)
@Composable
fun DialogFooterPreview() {
    DialogFooter(
        primaryButtonText = "Save",
        onCancel = {},
        enabled = true,
        onPrimaryClick = {}
    )
}