package org.whiskersapps.mordomo.mobile.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.screens.launcher.LauncherActivity
import org.whiskersapps.mordomo.mobile.ui.screens.setup.SetupActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        CoroutineScope(Dispatchers.Default).launch {
            val repo = get<SettingsRepository>()

            repo.loading.filter { true }.collect {
                val settings = get<SettingsRepository>().appSettings.value

                val activity = if (settings.setup.completed) {
                    LauncherActivity::class.java
                } else {
                    SetupActivity::class.java
                }

                startActivity(Intent(this@MainActivity, activity))
                finish()
            }
        }
    }
}