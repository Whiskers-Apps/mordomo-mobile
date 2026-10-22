package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.section(): Modifier {
    return this
        .clip(RoundedCornerShape(40.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
}