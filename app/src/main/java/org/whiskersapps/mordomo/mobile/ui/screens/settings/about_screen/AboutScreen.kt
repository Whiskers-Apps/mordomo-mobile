package org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.InfoCard
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme
import org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen.AboutScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen.AboutScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.settings.about_screen.AboutScreenVM as VM

@Composable
fun AboutScreenRoot(
    onBack: () -> Unit,
    vm: VM = koinViewModel()
) {
    LaunchedEffect(Unit) {
        vm.uiCallback.collect {
            when (it) {
                AboutScreenUICallback.GoBack -> onBack()
            }
        }
    }

    AboutScreen(vm.state.collectAsStateWithLifecycle().value) { vm.onIntent(it) }
}

@Composable
fun AboutScreen(
    state: State,
    onIntent: (Intent) -> Unit
) {
    if (!state.loading) {
        ContainerColumn(scrollable = true) {
            Nav("About") { onIntent(Intent.BackClick) }

            Column(
                Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    .section()
            ) {
                InfoCard(
                    iconId = R.drawable.hashtag,
                    title = "Version",
                    description = state.version
                )

                ListDivider()

                InfoCard(
                    iconId = R.drawable.paper,
                    title = "License",
                    description = "MIT"
                )

                ListDivider()

                InfoCard(
                    iconId = R.drawable.github,
                    title = "Repository",
                    description = state.repo,
                    onClick = { onIntent(Intent.URLClick) }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AboutScreenPreview() {
    MordomoTheme {
        AboutScreen(
            State(
                loading = false,
                version = "1.0.0",
                repo = "https://github.com/Whiskers-Apps/mordomo-mobile"
            )
        ) {}
    }
}