package org.whiskersapps.mordomo.mobile.core.features.widgets

import android.appwidget.AppWidgetProviderInfo

data class Widget(
    val id: Int,
    val name: String,
    val info: AppWidgetProviderInfo,
    val rows: Int,
    val cols: Int
)
