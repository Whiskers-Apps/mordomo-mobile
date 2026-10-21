package org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.BookmarksScreenState.EditBookmarkDialogState
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TextInput
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme
import org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.EditBookmarkDialogIntent as DialogIntent

@Composable
fun EditBookmarkDialog(
    state: EditBookmarkDialogState,
    onIntent: (DialogIntent) -> Unit
) {
    DialogLayout(
        onDismiss = { onIntent(DialogIntent.Close) },
    ) {
        DialogHeader(R.drawable.pencil, "Edit Bookmark")

        TextInput(
            value = state.name,
            onValueChange = { onIntent(DialogIntent.NameType(it)) },
            label = "Name",
            errorMessage = state.nameError
        )

        Spacer(Modifier.height(8.dp))

        TextInput(
            value = state.url,
            onValueChange = { onIntent(DialogIntent.URLType(it)) },
            label = "URL",
            errorMessage = state.urlError
        )

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier.height(48.dp),
            onClick = { onIntent(DialogIntent.DeleteClick) },
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
            colors = outlinedButtonColors(
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
@Preview(showSystemUi = true)
fun EditBookmarkDialogPreview() {
    MordomoTheme {
        EditBookmarkDialog(
            state = EditBookmarkDialogState(
                show = true,
                name = "Notion",
                url = "https://notion.so",
            )
        ) {}
    }
}