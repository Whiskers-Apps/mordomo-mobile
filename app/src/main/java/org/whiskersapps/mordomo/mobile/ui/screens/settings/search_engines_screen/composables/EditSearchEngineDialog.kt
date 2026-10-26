package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.SwitchSetting
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TextInput
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.EditEngineDialogIntent as DialogIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.SearchEnginesScreenState.EditEngineDialogState as DialogState


@Composable
fun EditSearchEngineDialog(
    state: DialogState,
    onIntent: (DialogIntent) -> Unit
) {
    DialogLayout(onDismiss = { onIntent(DialogIntent.Close) }) {
        DialogHeader("Edit Search Engine")

        TextInput(
            value = state.name,
            onValueChange = { onIntent(DialogIntent.NameType(it)) },
            label = "Name",
            errorMessage = state.nameError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(8.dp))

        TextInput(
            value = state.query,
            onValueChange = { onIntent(DialogIntent.QueryType(it)) },
            label = "Query",
            errorMessage = state.queryError
        )

        Spacer(Modifier.height(8.dp))

        SwitchSetting(
            padding = PaddingValues(start = 4.dp, end = 4.dp),
            backgroundColor = MaterialTheme.colorScheme.background,
            title = "Default",
            description = "Set this search engine as the preferred.",
            value = state.default,
            onValueChange = { onIntent(DialogIntent.DefaultToggle(it)) },
        )

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier.height(48.dp),
            onClick = { onIntent(DialogIntent.DeleteClick) },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Delete",
                textAlign = TextAlign.Center
            )
        }

        DialogFooter(
            primaryButtonText = "Save",
            onPrimaryClick = { onIntent(DialogIntent.SaveClick) },
            onCancel = { onIntent(DialogIntent.Close) }
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun EditSearchEngineDialogPreview() {
    EditSearchEngineDialog(
        state = DialogState(
            show = true,
            name = "Bazinga",
            query = "https://google.com/?search=%s",
        )
    ) {}
}
