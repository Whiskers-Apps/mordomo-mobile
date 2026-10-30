package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.droid.droid_icons.models.Icon
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.apps.App.Icons
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables.AppsPicker
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables.AppsPickerState
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.InfoCard
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
    state: State,
    onIntent: (Intent) -> Unit
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.showSecureAppsPicker, state.showHiddenAppsPicker) {
        snapshotFlow { state.showSecureAppsPicker || state.showHiddenAppsPicker }.collect { show ->
            scope.launch {
                if (show) {
                    sheetState.expand()
                } else {
                    sheetState.hide()
                }
            }
        }
    }

    LaunchedEffect(sheetState.currentValue) {
        snapshotFlow { sheetState.currentValue }
            .collect { target ->
                if (target == SheetValue.PartiallyExpanded) {
                    onIntent(HiddenAppsIntent.Close)
                    onIntent(SecureAppsIntent.Close)
                }
            }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetDragHandle = null,
        containerColor = MaterialTheme.colorScheme.background,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            Box(Modifier.systemBarsPadding()) {
                if (state.showHiddenAppsPicker) {
                    AppsPicker(
                        state = state.hiddenAppsPicker,
                        "Hidden Apps",
                        R.drawable.anonym,
                        onClose = { onIntent(HiddenAppsIntent.Close) },
                        onAppClick = { onIntent(HiddenAppsIntent.AppClick(it.packageName)) },
                        onSaveClick = { onIntent(HiddenAppsIntent.SaveClick) },
                    )
                }

                if (state.showSecureAppsPicker) {
                    AppsPicker(
                        state = state.secureAppsPicker,
                        "Secure Apps",
                        R.drawable.fingerprint,
                        onClose = { onIntent(SecureAppsIntent.Close) },
                        onAppClick = { onIntent(SecureAppsIntent.AppClick(it.packageName)) },
                        onSaveClick = { onIntent(SecureAppsIntent.SaveClick) },
                    )
                }
            }
        }
    ) { padding ->
        ContainerColumn(Modifier.padding(padding)) {
            Nav("Security") { onIntent(Intent.BackClick) }

            Column(
                Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    .section(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                InfoCard(
                    iconId = R.drawable.anonym,
                    title = "Hidden Apps",
                    description = "Select apps that should be hidden",
                    onClick = { onIntent(Intent.HiddenAppsClick) },
                )

                ListDivider()

                InfoCard(
                    iconId = R.drawable.fingerprint,
                    title = "Secure Apps",
                    description = "Select apps that require fingerprint to open",
                    onClick = { onIntent(Intent.SecureAppsClick) },
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SecurityScreenPreview() {
    MordomoTheme {
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
                hiddenAppsPicker = AppsPickerState(
                    apps = apps,
                    selectedApps = listOf("app 3", "app 5")
                )
            )
        ) {}
    }
}