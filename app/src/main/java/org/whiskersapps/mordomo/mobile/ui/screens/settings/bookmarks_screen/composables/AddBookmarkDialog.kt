package org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.BookmarksScreenState.AddBookmarkDialogState
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TextInput
import org.whiskersapps.mordomo.mobile.ui.screens.settings.bookmarks_screen.AddBookmarkDialogIntent as DialogIntent

@Composable
fun AddBookmarkDialog(
    state: AddBookmarkDialogState,
    onIntent: (DialogIntent) -> Unit
) {
    DialogLayout(
        onDismiss = { onIntent(DialogIntent.Close) },
    ) {
        DialogHeader("Add Bookmark")

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
            label = "Url",
            errorMessage = state.urlError
        )

        DialogFooter(
            primaryButtonText = "Save",
            onPrimaryClick = { onIntent(DialogIntent.SaveClick) },
            onCancel = { onIntent(DialogIntent.Close) }
        )
    }
}

@Composable
@Preview
fun AddBookmarkDialogPreview() {
    AddBookmarkDialog(
        state = AddBookmarkDialogState(
            show = true,
            name = "Notion",
            url = "https://notion.so",
        )
    ) { }
}