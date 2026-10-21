package org.whiskersapps.mordomo.mobile.ui.screens.settings.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

@Composable
fun Nav(
    text: String? = null,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        text?.let { text ->
            Spacer(Modifier.width(16.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.width(48.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NavPreview() {
    MordomoTheme {
        Nav("Bookmarks") { }
    }
}