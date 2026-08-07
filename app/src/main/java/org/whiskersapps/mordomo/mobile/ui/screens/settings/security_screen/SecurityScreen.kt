package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.App.Icons
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables.AppsPicker
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.IconSetting
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.CardShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogTitle
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ResponsiveColumn
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

    ResponsiveColumn() {
        Nav { onIntent(Intent.BackClick) }

        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            IconSetting(
                iconId = R.drawable.anonym,
                title = "Hidden Apps",
                description = "Select apps that should be hidden",
                cardShape = CardShape.Top,
            ) { onIntent(Intent.HiddenAppsClick) }

            IconSetting(
                iconId = R.drawable.fingerprint,
                title = "Secure Apps",
                description = "Select apps that require fingerprint to open",
                cardShape = CardShape.Bottom,
            ) { onIntent(Intent.SecureAppsClick) }
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
                        stock = org.whiskersapps.droid.droid_icons.models.Icon(
                            drawable = placeholder,
                            adaptive = null
                        ),
                        themed = null
                    )
                )
            )
        }

        temp
    }

    SecurityScreen(
        State(
            showHiddenAppsPicker = true,
            hiddenAppsPicker = State.Picker(
                apps = apps,
                selectedApps = setOf("app 3", "app 5")
            )
        )
    ) {}
}