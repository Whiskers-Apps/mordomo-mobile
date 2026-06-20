package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.components

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

@Composable
fun SectionHeader(
    @DrawableRes iconId: Int,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.width(16.dp))

        androidx.compose.material3.Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconId),
            contentDescription = null
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = title,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }

    Spacer(Modifier.height(8.dp))
}

@Composable
@Preview(showBackground = true)
fun SectionHeaderPreview() {
    SectionHeader(iconId = R.drawable.apps, "Apps")
}