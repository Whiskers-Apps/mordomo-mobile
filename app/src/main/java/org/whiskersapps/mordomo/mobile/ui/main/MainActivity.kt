package org.whiskersapps.mordomo.mobile.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.ui.main.launcher.LauncherActivity
import org.whiskersapps.mordomo.mobile.ui.main.setup.SetupActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val context = this

        lifecycleScope.launch {
            get<SettingsRepository>().appSettings.first().also { settings ->
                println(settings)
                val activity = if (settings.setup.completed) {
                    LauncherActivity::class.java
                } else {
                    SetupActivity::class.java
                }

                context.startActivity(
                    Intent(context, activity)
                )

                context.finish()
            }
        }
    }
}