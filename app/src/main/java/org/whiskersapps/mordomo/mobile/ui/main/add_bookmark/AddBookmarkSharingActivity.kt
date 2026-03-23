package org.whiskersapps.mordomo.mobile.ui.main.add_bookmark

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

class AddBookmarkSharingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    val url = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
                    val name = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""

                    setContent {
                        MordomoTheme {

                            AddBookmarkSharingScreenRoot(name = name, url = url)
                        }
                    }
                }
            }

            else -> throw RuntimeException("Could not find name and url from the action")
        }
    }
}