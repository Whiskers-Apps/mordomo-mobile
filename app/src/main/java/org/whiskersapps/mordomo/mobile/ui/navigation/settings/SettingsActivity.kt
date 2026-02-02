package org.whiskersapps.mordomo.mobile.ui.navigation.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MordomoTheme {
                Box(
                    contentAlignment = Alignment.Companion.Center,
                    modifier = Modifier.Companion.fillMaxSize()
                ) {
                    Text("Settings")
                }
            }
        }
    }
}