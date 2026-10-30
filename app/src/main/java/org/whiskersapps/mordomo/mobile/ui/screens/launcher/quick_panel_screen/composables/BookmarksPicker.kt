package org.whiskersapps.mordomo.mobile.ui.screens.launcher.quick_panel_screen.composables

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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.core.features.apps.App
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.utils.getWebsiteIconURL
import org.whiskersapps.mordomo.mobile.ui.shared_composables.AppIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.ListDivider
import org.whiskersapps.mordomo.mobile.ui.shared_composables.WebIcon
import org.whiskersapps.mordomo.mobile.ui.shared_composables.section

@Composable
fun BookmarksPicker(
    bookmarks: List<Bookmark>,
    selectedBookmarks: List<Int>,
    title: String,
    @DrawableRes selectedIconId: Int,
    onClose: () -> Unit,
    onBookmarkClick: (bookmark: Bookmark) -> Unit,
    onSaveClick: () -> Unit
) {
    BackHandler {
        onClose()
    }

    Column(
        Modifier
            .systemBarsPadding()
            .padding(24.dp)
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
            itemsIndexed(
                items = bookmarks,
                key = { _, bookmark -> bookmark.id }
            ) { index, bookmark ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable() {
                            onBookmarkClick(bookmark)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    WebIcon(
                        url = getWebsiteIconURL(bookmark.url),
                        size = 40.dp,
                        showRealIcon = true
                    )

                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = bookmark.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                    )

                    if (bookmark.id in selectedBookmarks) {
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

                if (index < bookmarks.size - 1) {
                    ListDivider()
                }
            }
        }
    }
}
