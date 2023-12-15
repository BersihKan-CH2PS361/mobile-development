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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Mercury
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.textRegularMedium

@Composable
fun PhoneNumberTextField(
    input: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var isFocused by remember {
        mutableStateOf(false)
    }

    val borderColor = if (isFocused) PacificBlue else Color.Transparent

    Column(
        modifier = modifier
            .height(90.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { onInputChange(it) },
            enabled = true,
            textStyle = textRegularMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            singleLine = true,
            maxLines = 1,
            shape = Shapes.small,
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_phone_number),
                    style = textRegularMedium,
                    color = Grey,
                    modifier = Modifier.offset(0.dp, 1.dp)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_telephone),
                    contentDescription = null,
                    tint = Grey,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(start = 12.dp)
                )
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
            modifier = Modifier
                .fillMaxWidth()
                .border((1.8).dp, borderColor, Shapes.small)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
        )
    }

}

@Preview
@Composable
fun PhoneNumberTextFieldPreview() {
    var input by remember {
        mutableStateOf("")
    }
    BersihKanTheme {
        PhoneNumberTextField(
            input = input,
            onInputChange = { newValue ->
                input = newValue
            }
        )
    }
}