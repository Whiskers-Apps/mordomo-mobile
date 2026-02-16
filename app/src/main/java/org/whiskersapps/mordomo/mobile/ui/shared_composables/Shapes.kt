package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

val TopCardShape =
    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 4.dp)

val BottomCardShape =
    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)

val MiddleCardShape =
    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 4.dp, bottomEnd = 4.dp)

val SingleCardShape =
    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)

val LeftCardShape =
    RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 4.dp)

val LeftCircleShape =
    RoundedCornerShape(topStart = 99.dp, topEnd = 4.dp, bottomStart = 99.dp, bottomEnd = 4.dp)

val RightCardShape =
    RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)


val RightCircleShape =
    RoundedCornerShape(topStart = 4.dp, topEnd = 99.dp, bottomStart = 4.dp, bottomEnd = 99.dp)


fun getShapeByIndex(index: Int, listSize: Int): RoundedCornerShape {
    return if (index == 0 && listSize == 1) {
        SingleCardShape
    } else if (index == 0) {
        TopCardShape
    } else if (index < listSize - 1) {
        MiddleCardShape
    } else {
        BottomCardShape
    }
}