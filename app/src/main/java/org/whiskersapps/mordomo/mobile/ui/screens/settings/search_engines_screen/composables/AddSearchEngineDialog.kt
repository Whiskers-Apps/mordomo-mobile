package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TextInput
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.AddEngineDialogIntent as DialogIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.SearchEnginesScreenState.AddEngineDialogState as DialogState


@Composable
fun AddSearchEngineDialog(
    state: DialogState,
    onIntent: (DialogIntent) -> Unit
) {
    DialogLayout(onDismiss = { onIntent(DialogIntent.Close) }) {
        DialogHeader("Add Search Engine")

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

        DialogFooter(
            primaryButtonText = "Save",
            onPrimaryClick = { onIntent(DialogIntent.SaveClick) },
            onCancel = { onIntent(DialogIntent.Close) }
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun AddSearchEngineDialogPreview() {
    MordomoTheme {
        AddSearchEngineDialog(
            state = DialogState(
                show = true,
                name = "Bazinga",
                query = "https://google.com/?search=%s",
            )
        ) {}
    }
}