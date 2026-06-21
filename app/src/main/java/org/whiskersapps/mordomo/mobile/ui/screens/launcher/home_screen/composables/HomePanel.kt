package org.whiskersapps.mordomo.mobile.ui.screens.launcher.home_screen.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.shared_composables.LeftCircleShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.MiddleCardShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.RightCircleShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.conditionalModifier

@Composable
fun HomePanel(
    showQuickButton: Boolean,
    showSearchButton: Boolean,
    showAppsButton: Boolean,
    onQuickPanelButtonClick: () -> Unit,
    onSearchButtonClick: () -> Unit,
    onAppsButtonClick: () -> Unit
) {
    val quickShape by
    remember(key1 = showSearchButton, key2 = showAppsButton) {
        derivedStateOf {
            if (showSearchButton || showAppsButton) {
                LeftCircleShape
            } else {
                CircleShape
            }
        }
    }

    val searchShape by
    remember(key1 = showQuickButton, key2 = showAppsButton) {
        derivedStateOf {
            if (!showQuickButton && showAppsButton) {
                LeftCircleShape
            } else if (showQuickButton && !showAppsButton) {
                RightCircleShape
            } else if (showQuickButton && showAppsButton) {
                MiddleCardShape
            } else {
                CircleShape
            }
        }
    }

    val appsShape by
    remember(key1 = showQuickButton, key2 = showSearchButton) {
        derivedStateOf {
            if (showQuickButton || showSearchButton) {
                RightCircleShape
            } else {
                CircleShape
            }
        }
    }



    Row(Modifier.fillMaxWidth()) {
        if (showQuickButton) {
            OutlinedButton(
                modifier = Modifier
                    .height(56.dp)
                    .conditionalModifier(showSearchButton) {
                        this.wrapContentWidth()
                    }
                    .conditionalModifier(!showSearchButton) {
                        this.weight(1f, fill = true)
                    },
                onClick = { onQuickPanelButtonClick() },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                shape = quickShape
            ) {

                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.lightning),
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text(text = "Quick", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Spacer(Modifier.width(4.dp))

        }

        if (showSearchButton) {
            OutlinedButton(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                onClick = { onSearchButtonClick() },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                shape = searchShape
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.search),
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Search",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(4.dp))
        }

        if (showAppsButton) {
            OutlinedButton(
                modifier = Modifier
                    .height(56.dp)
                    .conditionalModifier(showSearchButton) {
                        this.wrapContentWidth()
                    }
                    .conditionalModifier(!showSearchButton) {
                        this.weight(1f, fill = true)
                    },
                onClick = { onAppsButtonClick() },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                shape = appsShape
            ) {

                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.apps),
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text(text = "Apps", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
@Preview
fun HomePanelPreview() {
    HomePanel(
        showQuickButton = true,
        showSearchButton = true,
        showAppsButton = true,
        onQuickPanelButtonClick = {},
        onSearchButtonClick = {},
        onAppsButtonClick = {}
    )
}