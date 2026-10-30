package org.whiskersapps.mordomo.mobile.core.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.core.net.toUri

fun isDefaultLauncher(context: Context): Boolean {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }

    val resolve = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

    return resolve?.activityInfo?.packageName == context.packageName
}

fun String.isFullURL(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
            && (this.startsWith("https://") || this.startsWith("http://"))
}


fun String.isShortURL(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}

fun getWebsiteIconURL(url: String): String {
    val trimmedURL = url.removePrefix("https://").removeSuffix("http://")

    return "https://favicon.is/$trimmedURL"
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun openURL(url: String, context: Context) {
    try {
        val parsedURL = if (!url.startsWith("https") || !url.startsWith("http")) {
            "https://${url}"
        } else {
            url
        }

        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                parsedURL.toUri()
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    } catch (e: Exception) {
        Log.e("Search Screen", "Error opening url")
        e.printStackTrace()
    }
}