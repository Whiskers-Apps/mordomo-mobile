package org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.search_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

@Composable
fun SectionHeader(
    title: String
) {
    Row {
        Spacer(Modifier.width(8.dp))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Spacer(Modifier.height(4.dp))
}

@Composable
@Preview(showBackground = true)
fun SectionHeaderPreview() {
    MordomoTheme {
        SectionHeader("Apps")
    }
}