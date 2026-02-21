package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun Modifier.modifyIf(condition: Boolean, modifier: @Composable Modifier.() -> Modifier): Modifier {
    return if (condition) then(modifier()) else this
}

suspend fun hideKeyboard(
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) = withContext(Dispatchers.Main) {
    focusManager.clearFocus()
    keyboardController?.hide()
}