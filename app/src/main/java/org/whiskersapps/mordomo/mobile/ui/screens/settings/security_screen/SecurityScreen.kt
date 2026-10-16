package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.droid.droid_icons.models.Icon
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.App.Icons
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables.AppsPicker
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.IconSetting
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.BOTTOM_SHAPE
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.InfoCard
import org.whiskersapps.mordomo.mobile.ui.shared_composables.TOP_SHAPE
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenIntent as Intent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenState as State
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenVM as VM

@Composable
fun SecurityScreenRoot(
    onBack: () -> Unit,
    vm: VM = koinViewModel()
) {
    LaunchedEffect(Unit) {
        vm.uiCallback.collect {
            when (it) {
                SecurityScreenUICallback.GoBack -> onBack()
            }
        }
    }

    SecurityScreen(vm.state.collectAsStateWithLifecycle().value) { vm.onIntent(it) }
}

@Composable
fun SecurityScreen(
    state: State,
    onIntent: (Intent) -> Unit
) {

    if (state.showHiddenAppsPicker) {
        AppsPicker(
            picker = state.hiddenAppsPicker,
            "Hidden Apps",
            R.drawable.anonym,
            onClose = { onIntent(HiddenAppsIntent.Close) },
            onAppClick = { onIntent(HiddenAppsIntent.AppClick(it.packageName)) },
            onSaveClick = { onIntent(HiddenAppsIntent.SaveClick) },
        )
    }

    if (state.showSecureAppsPicker) {
        AppsPicker(
            picker = state.secureAppsPicker,
            "Secure Apps",
            R.drawable.fingerprint,
            onClose = { onIntent(SecureAppsIntent.Close) },
            onAppClick = { onIntent(SecureAppsIntent.AppClick(it.packageName)) },
            onSaveClick = { onIntent(SecureAppsIntent.SaveClick) },
        )
    }

    ContainerColumn {
        Nav { onIntent(Intent.BackClick) }

        Column(
            Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            InfoCard(
                shape = TOP_SHAPE,
                title = "Hidden Apps",
                description = "Select apps that should be hidden",
                onClick = { onIntent(Intent.HiddenAppsClick) },
            )

            InfoCard(
                shape = BOTTOM_SHAPE,
                title = "Secure Apps",
                description = "Select apps that require fingerprint to open",
                onClick = { onIntent(Intent.SecureAppsClick) },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SecurityScreenPreview() {
    val context = LocalContext.current
    val placeholder = ContextCompat.getDrawable(context, R.drawable.logo_tie)!!

    val apps = remember {
        val temp = ArrayList<App>()
        for (i in 0..23) {
            temp.add(
                App(
                    name = "App $i",
                    packageName = "app $i",
                    shortcuts = listOf(),
                    icons = Icons(
                        stock = Icon(
                            drawable = placeholder,
                            adaptive = null
                        ),
                        themed = null
                    ),
                    widgets = emptyList()
                )
            )
        }

        temp
    }

    SecurityScreen(
        State(
            showHiddenAppsPicker = false,
            hiddenAppsPicker = State.Picker(
                apps = apps,
                selectedApps = setOf("app 3", "app 5")
            )
        )
    ) {}
}