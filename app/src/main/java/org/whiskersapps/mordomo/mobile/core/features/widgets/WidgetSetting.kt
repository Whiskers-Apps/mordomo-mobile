package org.whiskersapps.mordomo.mobile.core.features.widgets

import kotlinx.serialization.Serializable

@Serializable
data class WidgetSetting(
    val index: Int,
    val id: Int,
    val name: String,
    val cols: Int,
    val rows: Int
)
