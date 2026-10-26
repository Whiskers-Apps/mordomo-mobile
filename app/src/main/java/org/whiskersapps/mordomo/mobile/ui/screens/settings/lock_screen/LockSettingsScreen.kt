package org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.Nav
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.SegmentedOption
import org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables.SegmentedSetting
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ContainerColumn
import org.whiskersapps.mordomo.mobile.ui.shared_composables.InfoMessage
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme
import org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen.LockSettingsScreenIntent as ScreenIntent
import org.whiskersapps.mordomo.mobile.ui.screens.settings.lock_screen.LockSettingsScreenState as ScreenState


@Composable
fun LockSettingsScreenRoot(
    onBack: () -> Unit,
    vm: LockSettingsScreenVM = koinViewModel()
) {
    LaunchedEffect(Unit) {
        vm.uiCallback.collect { uICallback ->
            when (uICallback) {
                LockSettingsScreenUICallback.GoBack -> onBack()
            }
        }
    }

    LockSettingsScreen(
        state = vm.state.collectAsState().value,
        onIntent = { vm.onIntent(it) }
    )
}

@Composable
fun LockSettingsScreen(
    state: ScreenState,
    onIntent: (ScreenIntent) -> Unit
) {
    if (!state.loading) {
        ContainerColumn {
            Nav("Lock") { onIntent(ScreenIntent.Back) }

            Column(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {
                Column(Modifier.section()) {
                    SegmentedSetting(
                        shape = RoundedCornerShape(0.dp),
                        title = "Method",
                        description = "Select the lock screen method when double clicking in the home screen.",
                        activeOptionId = state.lockMethod,
                        options = listOf(
                            SegmentedOption(
                                id = SettingsValues.LockScreenMethod.DISABLED,
                                text = "Off"
                            ),
                            SegmentedOption(
                                id = SettingsValues.LockScreenMethod.ROOT,
                                text = "Root"
                            ),
                            SegmentedOption(
                                id = SettingsValues.LockScreenMethod.ACCESSIBILITY,
                                text = "Accessibility"
                            )
                        ),
                        onChange = { onIntent(ScreenIntent.MethodSelect(it)) },
                    )
                }

                state.warningMessage?.let {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = state.warningMessage,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(8.dp))

                if (state.lockMethod != SettingsValues.LockScreenMethod.DISABLED) {
                    Button(
                        onClick = { onIntent(ScreenIntent.TestLockClick) }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.lock_outline),
                            contentDescription = null
                        )

                        Spacer(Modifier.width(8.dp))

                        Text("Test Lock", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun PreviewLockSettingsScreen() {
    val method = SettingsValues.LockScreenMethod.ROOT

    MordomoTheme {
        LockSettingsScreen(
            ScreenState(
                loading = false,
                lockMethod = method,
                warningMessage = when (method) {
                    SettingsValues.LockScreenMethod.ROOT -> "This method requires root access. The second best option is the accessibility one."
                    SettingsValues.LockScreenMethod.ACCESSIBILITY -> "This method requires accessibility access. This is the ONLY thing the app uses it for."
                    else -> null
                }
            )
        ) {}
    }
}