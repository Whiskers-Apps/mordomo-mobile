package org.whiskersapps.mordomo.mobile.ui.main_screen.add_bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.whiskersapps.mordomo.mobile.core.utils.getActivity
import org.whiskersapps.mordomo.mobile.core.utils.getWebsiteIconURL
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogTitle
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.main_screen.add_bookmark.AddBookmarkSharingScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.main_screen.add_bookmark.AddBookmarkSharingScreenState as ScreenState
import org.whiskersapps.mordomo.mobile.ui.main_screen.add_bookmark.AddBookmarkSharingScreenVM as ScreenVM

@Composable
fun AddBookmarkSharingScreenRoot(
    name: String,
    url: String,
    vm: ScreenVM = koinViewModel { parametersOf(name, url) }
) {
    val state = vm.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(state.killFlag) {
        if (state.killFlag) {
            context.getActivity()?.finish()
        }
    }

    AddBookmarkSharingScreen(state) { intent ->
        vm.onIntent(intent)
    }
}

@Composable
fun AddBookmarkSharingScreen(state: ScreenState, onIntent: (ScreenIntent) -> Unit) {
    ResponsiveColumn(contentPadded = true) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            DialogTitle("Add Bookmark")

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                onValueChange = { onIntent(ScreenIntent.NameType(it)) },
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
                onValueChange = { onIntent(ScreenIntent.URLType(it)) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                label = {
                    Text("URL")
                }
            )

            Spacer(Modifier.height(16.dp))

            if (state.saveEnabled) {
                WebIcon(getWebsiteIconURL(state.url), size = 48.dp)
            }
        }

        DialogFooter(
            onPrimaryClick = { onIntent(ScreenIntent.SaveClick) },
            primaryButtonText = "Save",
            enabled = state.saveEnabled,
            onCancel = { onIntent(ScreenIntent.CancelClick) }
        )
    }
}

@Composable
@Preview(showBackground = true)
@Preview(
    device = "spec:width=910dp,height=351dp,orientation=landscape,dpi=420",
    showBackground = true
)
fun AddBookmarkSharingScreenPreview() {
    AddBookmarkSharingScreen(
        state = ScreenState(
            name = "Banana",
            url = "https://google.com",
            saveEnabled = true
        )
    ) {}
}