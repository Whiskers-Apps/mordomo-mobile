package org.whiskersapps.mordomo.mobile.ui.shared_composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.whiskersapps.mordomo.mobile.R
import org.whiskersapps.mordomo.mobile.ui.theme.MordomoTheme

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = { focusManager.clearFocus() }
    )
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { onValueChange(it) },
        shape = CircleShape,
        singleLine = true,
        isError = errorMessage != null,
        supportingText = errorMessage?.let { message ->
            {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onValueChange("") },
                    painter = painterResource(R.drawable.clear_circle),
                    contentDescription = null,
                    tint = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            errorBorderColor = MaterialTheme.colorScheme.error
        )
    )
}

@Composable
@Preview
fun PreviewTextInput() {
    MordomoTheme {
        Column {
            TextInput(
                value = "Banana",
                onValueChange = {},
                label = "Name"
            )

            TextInput(
                value = "something@@.com",
                onValueChange = {},
                label = "Email",
                errorMessage = "Invalid Email"
            )
        }
    }
}