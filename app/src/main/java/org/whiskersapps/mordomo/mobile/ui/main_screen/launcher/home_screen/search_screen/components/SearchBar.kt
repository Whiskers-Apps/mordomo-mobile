package org.whiskersapps.mordomo.mobile.ui.main_screen.launcher.home_screen.search_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R

@Composable
fun SearchBar(
    value: String,
    onValueChange: (text: String) -> Unit,
    focusRequester: FocusRequester,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .focusRequester(focusRequester),
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text("Search")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.background,
            unfocusedBorderColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        ),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.search),
                contentDescription = null
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    modifier = Modifier.size(20.dp).clickable{
                        onValueChange("")
                    },
                    painter = painterResource(R.drawable.cross),
                    contentDescription = null
                )
            }
        },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        )
    )
}