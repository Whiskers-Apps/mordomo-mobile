package org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.composables

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
import org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.BookmarksScreenState.EditBookmarkDialogState
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DeletionSlider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
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

        Spacer(Modifier.height(8.dp))

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

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.url,
            onValueChange = { onIntent(DialogIntent.URLType(it)) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            label = {
                Text("URL")
            }
        )

        Spacer(Modifier.height(8.dp))

        DeletionSlider { onIntent(DialogIntent.DeleteSlide) }

        Spacer(Modifier.height(8.dp))

        DialogFooter(
            primaryButtonText = "Save",
            onPrimaryClick = { onIntent(DialogIntent.SaveClick) },
            enabled = state.saveEnabled,
            onCancel = { onIntent(DialogIntent.Close) }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun EditBookmarkDialogPreview() {
    EditBookmarkDialog(
        state = EditBookmarkDialogState(
            show = true,
            name = "Notion",
            url = "https://notion.so",
            saveEnabled = true
        )
    ) {}
}