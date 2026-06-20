package org.whiskersapps.mordomo.mobile.core.features.icons

import android.content.Context
import android.content.pm.LauncherApps
import android.graphics.drawable.AdaptiveIconDrawable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.whiskersapps.droid.droid_icons.IconFetcher
import org.whiskersapps.droid.droid_icons.models.Icon
import org.whiskersapps.droid.droid_icons.models.IconPack
import android.os.Process
import android.util.Log

class IconsRepository(
    private val context: Context
) {
    val iconFetcher = IconFetcher(context)
    private var iconPack: IconPack? = null

    private val _iconPacks = MutableStateFlow(emptyList<IconPack>())
    val iconPacks = _iconPacks.asStateFlow()

    init {
        _iconPacks.update { iconFetcher.getIconPacks() }
        iconPack = iconPacks.value.firstOrNull { it.packageName == "" }
    }

    fun getStockIcon(packageName: String): Icon {
        return iconFetcher.getStockIcon(packageName)
    }

    fun getThemedIcon(packageName: String): Icon? {
        return iconPack?.let {
            iconFetcher.getThemedIcon(it, packageName)
        }
    }
}
