package org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.composables

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.ui.screens.settings.security_screen.SecurityScreenState.Picker
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogFooter
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogLayout
import org.whiskersapps.mordomo.mobile.ui.shared_composables.DialogHeader
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.bubbleShape
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme


@Composable
fun AppsPicker(
    picker: Picker,
    title: String,
    @DrawableRes selectedIconId: Int,
    onClose: () -> Unit,
    onAppClick: (app: App) -> Unit,
    onSaveClick: () -> Unit
) {
    BackHandler {
        onClose()
    }

    Column(
        Modifier.padding(24.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.cross),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.width(16.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onSaveClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .section(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(items = picker.apps, key = { _, app -> app.packageName }) { index, app ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable() {
                            onAppClick(app)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        themed = false,
                        size = 40.dp,
                        icons = app.icons,
                        shape = CircleShape,
                    )

                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = app.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                    )

                    if (app.packageName in picker.selectedApps) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(selectedIconId),
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = null
                            )
                        }

                    }
                }

                if (index < picker.apps.size - 1) {
                    ListDivider()
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewAppsPicker() {
    val context = LocalContext.current
    val placeholder = ContextCompat.getDrawable(context, R.drawable.logo_tie)!!

    val apps = remember {
        val temp = ArrayList<App>()
        for (i in 0..23) {
            temp.add(
                App(
                    name = "App $i",
                    packageName = "app $i",
                    shortcuts = listOf(),
                    icons = App.Icons(
                        stock = org.whiskersapps.droid.droid_icons.models.Icon(
                            drawable = placeholder,
                            adaptive = null
                        ),
                        themed = null
                    ),
                    widgets = emptyList()
                )
            )
        }

        temp
    }

    MordomoTheme() {
        AppsPicker(
            picker = Picker(apps = apps, selectedApps = setOf("app 1", "app 3")),
            "Hidden Apps",
            R.drawable.anonym,
            onClose = {},
            onAppClick = {},
            onSaveClick = {},
        )
    }
}