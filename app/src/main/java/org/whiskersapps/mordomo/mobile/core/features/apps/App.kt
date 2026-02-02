package org.whiskersapps.mordomo.mobile.core.features.apps

import android.graphics.Bitmap
import org.whiskersapps.droid.droid_icons.models.Icon

data class App(
    val name: String,
    val packageName: String,
    val shortcuts: List<Shortcut>,
    val icons: Icons
) {
    data class Shortcut(
        val id: String,
        val text: String,
        val icon: Bitmap?
    )

    data class Icons(
        val stock: Icon,
        val themed: Icon?
    )
}
