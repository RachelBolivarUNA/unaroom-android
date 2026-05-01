package com.moviles.unaroom.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import com.moviles.unaroom.core.UserMessages
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun LoginEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppTextField(
        value = value,
        label = UserMessages.LoginUi.EMAIL_LABEL,
        placeholder = UserMessages.LoginUi.EMAIL_PLACEHOLDER,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun LoginPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppTextField(
        value = value,
        label = UserMessages.LoginUi.PASSWORD_LABEL,
        placeholder = "\u2022\u2022\u2022\u2022\u2022\u2022\u2022",
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun LoginSubmitButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppButton(
        text = if (isLoading) {
            UserMessages.LoginUi.BUTTON_LOADING
        } else {
            UserMessages.LoginUi.BUTTON_LOGIN
        },
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier.fillMaxWidth()
    )
}
