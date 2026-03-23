package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.revenuecat.purchases.slidetounlock.DefaultSlideToUnlockColors
import com.revenuecat.purchases.slidetounlock.HintTexts
import com.revenuecat.purchases.slidetounlock.SlideToUnlock
import org.whiskersapps.mordomo.mobile.R

@Composable
fun DeletionSlider(
    onSlideComplete: () -> Unit
) {
    var isSlided by remember { mutableStateOf(false) }

    SlideToUnlock(
        modifier = Modifier
            .fillMaxWidth()
            .border(color = MaterialTheme.colorScheme.error, shape = CircleShape, width = 2.dp)
            .padding(2.dp),
        isSlided = isSlided,
        onSlideCompleted = { onSlideComplete() },
        colors = DefaultSlideToUnlockColors(
            trackBrush = SolidColor(MaterialTheme.colorScheme.background),
            startHintColor = MaterialTheme.colorScheme.error
        ),
        hintTexts = HintTexts(
            defaultText = "Slide to Delete",
            slidedText = "Deleting"
        ),
        thumb = { _, _, _, _, _ ->
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
                    .padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.trash),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    )
}