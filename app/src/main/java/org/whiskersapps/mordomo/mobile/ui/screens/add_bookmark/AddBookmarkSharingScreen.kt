package org.whiskersapps.mordomo.mobile.ui.screens.add_bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.utils.getActivity
import org.whiskersapps.mordomo.mobile.core.utils.getWebsiteIconURL
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TextInput
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.screens.add_bookmark.AddBookmarkSharingScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.add_bookmark.AddBookmarkSharingScreenState as ScreenState
import org.whiskersapps.mordomo.mobile.ui.screens.add_bookmark.AddBookmarkSharingScreenVM as ScreenVM

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
fun AddBookmarkSharingScreen(
    state: ScreenState,
    onIntent: (ScreenIntent) -> Unit
) {
    ContainerColumn(contentPadded = true) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            DialogHeader(R.drawable.plus, "Add Bookmark")

            TextInput(
                value = state.name,
                onValueChange = { onIntent(ScreenIntent.NameType(it)) },
                label = "Name",
                errorMessage = state.nameError
            )

            Spacer(Modifier.height(8.dp))

            TextInput(
                value = state.url,
                onValueChange = { onIntent(ScreenIntent.URLType(it)) },
                label = "Url",
                errorMessage = state.urlError
            )

            Spacer(Modifier.height(8.dp))

            if (state.urlError == null) {
                WebIcon(getWebsiteIconURL(state.url), size = 48.dp)
            }
        }

        DialogFooter(
            onPrimaryClick = { onIntent(ScreenIntent.SaveClick) },
            primaryButtonText = "Save",
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
        )
    ) {}
}