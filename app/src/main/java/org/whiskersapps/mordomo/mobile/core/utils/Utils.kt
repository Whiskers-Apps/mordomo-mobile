package org.whiskersapps.mordomo.mobile.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

fun isDefaultLauncher(context: Context): Boolean {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }

    val resolve = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

    return resolve?.activityInfo?.packageName == context.packageName
}