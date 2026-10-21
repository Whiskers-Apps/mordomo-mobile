package org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.shared_composables.VerticalListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.conditionalModifier
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

@Composable
fun HomePanel(
    showQuickButton: Boolean,
    showSearchButton: Boolean,
    showAppsButton: Boolean,
    onQuickPanelButtonClick: () -> Unit,
    onSearchButtonClick: () -> Unit,
    onAppsButtonClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .heightIn(min = 48.dp)
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (showQuickButton) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .conditionalModifier(showSearchButton) {
                        this.wrapContentWidth()
                    }
                    .conditionalModifier(!showSearchButton) {
                        this.weight(1f, fill = true)
                    }
                    .clickable {
                        onQuickPanelButtonClick()
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.lightning),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Quick",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            VerticalListDivider(MaterialTheme.colorScheme.surfaceVariant)
        }

        if (showSearchButton) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .weight(1f, fill = true)
                    .clickable {
                        onSearchButtonClick()
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Search",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            VerticalListDivider(MaterialTheme.colorScheme.surfaceVariant)
        }

        if (showAppsButton) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .conditionalModifier(showSearchButton) {
                        this.wrapContentWidth()
                    }
                    .conditionalModifier(!showSearchButton) {
                        this.weight(1f, fill = true)
                    }
                    .clickable {
                        onAppsButtonClick()
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.apps),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Apps",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview
fun HomePanelPreview() {
    MordomoTheme {
        HomePanel(
            showQuickButton = true,
            showSearchButton = true,
            showAppsButton = true,
            onQuickPanelButtonClick = {},
            onSearchButtonClick = {},
            onAppsButtonClick = {}
        )
    }
}