package com.example.bersihkan.ui.components.textFields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Mercury
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Red
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.textRegularExtraSmall
import com.example.bersihkan.ui.theme.textRegularMedium
import com.example.bersihkan.helper.isPasswordValid

@Composable
fun PasswordTextField(
    input: String,
    onInputChange: (String) -> Unit,
    isError: Boolean,
    placeholderText: String,
    modifier: Modifier = Modifier
) {

    var isFocused by remember {
        mutableStateOf(false)
    }

    var isTextVisible by remember {
        mutableStateOf(false)
    }

    val borderColor = if (isFocused) PacificBlue else Color.Transparent
    val currentBorderColor = if(isError) Red else borderColor

    Column(
        modifier = modifier
            .height(90.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { onInputChange(it) },
            enabled = true,
            textStyle = textRegularMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            singleLine = true,
            maxLines = 1,
            shape = Shapes.small,
            placeholder = {
                Text(
                    text = placeholderText,
                    style = textRegularMedium,
                    color = Grey,
                    modifier = Modifier.offset(0.dp, 1.dp)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_password),
                    contentDescription = null,
                    tint = Grey,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(start = 12.dp)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { isTextVisible = !isTextVisible }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isTextVisible) R.drawable.ic_password_visible else R.drawable.ic_password_not_visible
                        ),
                        tint = Grey,
                        contentDescription = if (isTextVisible) "Hide password" else "Show password",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp)
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.Black,
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                errorBorderColor = Color.Red,
                focusedContainerColor = Mercury,
                unfocusedContainerColor = Mercury,
                errorContainerColor = Mercury,
            ),
            isError = isError,
            visualTransformation = if (isTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .border((1.8).dp, currentBorderColor, Shapes.small)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
        )
        if (isError) {
            Text(
                text = stringResource(R.string.invalid_password), // Error message to display
                color = Red,
                style = textRegularExtraSmall,
                modifier = Modifier
                    .padding(top = 7.dp)
            )
        }
    }

}

@Preview
@Composable
fun PasswordTextFieldPreview() {

    var password by remember {
        mutableStateOf("")
    }

    var isError by remember {
        mutableStateOf(false)
    }

    BersihKanTheme {
        PasswordTextField(
            input = password,
            onInputChange = { newValue ->
                password = newValue
                isError = !isPasswordValid(password)
            },
            isError = isError,
            placeholderText = stringResource(id = R.string.enter_confirm_password)
        )
    }

}