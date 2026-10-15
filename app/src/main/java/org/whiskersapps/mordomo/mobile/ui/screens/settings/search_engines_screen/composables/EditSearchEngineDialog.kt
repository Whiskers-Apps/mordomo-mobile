package org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.composables

import androidx.compose.foundation.layout.PaddingValues
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
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.SwitchSetting
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DeletionSlider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.EditEngineDialogIntent as DialogIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.search_engines_screen.SearchEnginesScreenState.EditEngineDialogState as DialogState


@Composable
fun EditSearchEngineDialog(
    state: DialogState,
    onIntent: (DialogIntent) -> Unit
) {
    DialogLayout(onDismiss = { onIntent(DialogIntent.Close) }) {
        DialogHeader(R.drawable.pencil,"Edit Search Engine")

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.name,
            onValueChange = { onIntent(DialogIntent.NameType(it)) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            label = {
                Text("Name")
            }
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.query,
            onValueChange = { onIntent(DialogIntent.QueryType(it)) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            label = {
                Text("Query")
            }
        )

        Spacer(Modifier.height(16.dp))

        SwitchSetting(
            title = "Default",
            description = "Set this search engine as the preferred.",
            value = state.default,
            onValueChange = { onIntent(DialogIntent.DefaultToggle(it)) },
            padding = PaddingValues(0.dp)
        )

        Spacer(Modifier.height(16.dp))

        DeletionSlider { onIntent(DialogIntent.DeleteSlide) }

        Spacer(Modifier.height(16.dp))

        DialogFooter(
            primaryButtonText = "Save",
            onPrimaryClick = { onIntent(DialogIntent.SaveClick) },
            enabled = state.saveEnabled,
            onCancel = { onIntent(DialogIntent.Close) }
        )
    }
}

@Composable
@Preview
fun EditSearchEngineDialogPreview() {
    EditSearchEngineDialog(
        state = DialogState(
            show = true,
            name = "Bazinga",
            query = "https://google.com/?search=%s",
            saveEnabled = true
        )
    ) {}
}
