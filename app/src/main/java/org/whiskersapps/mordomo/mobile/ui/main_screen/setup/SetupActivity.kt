package org.whiskersapps.mordomo.mobile.ui.main_screen.setup

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.LauncherActivity
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

class SetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        fun open() {
            this.startActivity(Intent(this, LauncherActivity::class.java))
            this.finish()
        }

        setContent {
            MordomoTheme {
                val scope = rememberCoroutineScope()

                Column(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .systemBarsPadding()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier.Companion
                            .fillMaxHeight()
                            .weight(1f, fill = true)
                    )

                    Icon(
                        modifier = Modifier.Companion.size(128.dp),
                        painter = painterResource(R.drawable.logo_tie),
                        contentDescription = "logo",
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.Companion.height(16.dp))

                    Text(
                        "Here To Serve You",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(
                        modifier = Modifier.Companion
                            .fillMaxHeight()
                            .weight(1f, fill = true)
                    )

                    Button(
                        onClick = {
                            scope.launch {
                                get<SettingsRepository>().setSetupCompleted(true)
                            }

                            open()

                        }
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}