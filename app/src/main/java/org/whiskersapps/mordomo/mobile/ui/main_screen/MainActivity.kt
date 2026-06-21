package org.whiskersapps.mordomo.mobile.ui.main_screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.LauncherActivity
import org.whiskersapps.mordomo.mobile.ui.main_screen.setup.SetupActivity

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