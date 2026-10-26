package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme


@Composable
fun DialogHeader(
    title: String
) {
    Column{
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun DialogFooter(
    primaryButtonText: String = "",
    onCancel: () -> Unit,
    enabled: Boolean = true,
    onPrimaryClick: () -> Unit = {},
) {
    Column {
        Spacer(Modifier.height(8.dp))

        Column {
            if (primaryButtonText.isNotEmpty()) {
                Button(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                    onClick = { onPrimaryClick() },
                    enabled = enabled
                ) {
                    Text(text = primaryButtonText, style = MaterialTheme.typography.labelLarge)
                }

                Spacer(Modifier.height(8.dp))
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth().heightIn(48.dp),
                onClick = { onCancel() },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            ) {
                Text(text = stringResource(R.string.Cancel))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DialogHeaderPreview() {
    MordomoTheme {
        DialogHeader(
            title = "Select Category"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DialogFooterPreview() {
    MordomoTheme {
        DialogFooter(
            primaryButtonText = "Save",
            onCancel = {},
            enabled = true,
            onPrimaryClick = {}
        )
    }
}