package org.whiskersapps.mordomo.mobile.ui.navigation.launcher.home_screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.shared_composables.LeftCircleShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.MiddleCardShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.RightCircleShape

@Composable
fun HomePanel(
    onQuickPanelButtonClick: () -> Unit,
    onSearchButtonClick: () -> Unit,
    onAppsButtonClick: () -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        OutlinedButton(
            modifier = Modifier
                .height(56.dp)
                .wrapContentWidth(),
            onClick = { onQuickPanelButtonClick() },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            shape = LeftCircleShape
        ) {

            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.lightning),
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(text = "Quick", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        Spacer(Modifier.width(4.dp))

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
            shape = MiddleCardShape
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
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

        OutlinedButton(
            modifier = Modifier
                .height(56.dp)
                .wrapContentWidth(),
            onClick = { onAppsButtonClick() },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            shape = RightCircleShape
        ) {

            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.apps),
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(text = "Apps", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}