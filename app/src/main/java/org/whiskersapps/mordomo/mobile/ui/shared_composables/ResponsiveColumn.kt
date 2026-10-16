package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveColumn(
    modifier: Modifier = Modifier,
    contentPadded: Boolean = false,
    scrollable: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .conditionalModifier(scrollable) {
                this.verticalScroll(rememberScrollState())
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .widthIn(max = 900.dp)
                .fillMaxWidth()
                .conditionalModifier(contentPadded) {
                    this.padding(24.dp)
                }
        ) {
            content()
        }
    }
}