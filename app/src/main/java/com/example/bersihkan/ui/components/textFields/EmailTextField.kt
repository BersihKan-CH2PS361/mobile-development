package com.example.bersihkan.ui.components.textFields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.bersihkan.ui.theme.Red
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineLarge
import com.example.bersihkan.ui.theme.textRegularExtraSmall
import com.example.bersihkan.ui.theme.textRegularMedium
import com.example.bersihkan.helper.isEmailValid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(
    input: String,
    onInputChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {

    var isFocused by remember {
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
            singleLine = true,
            maxLines = 1,
            shape = Shapes.small,
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_email),
                    style = textRegularMedium,
                    color = Grey,
                    modifier = Modifier.offset(0.dp, 1.dp)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = null,
                    tint = Grey,
                    modifier = Modifier
                        .size(34.dp)
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
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .border((1.8).dp, currentBorderColor, Shapes.small)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
        )
        if (isError) {
            Text(
                text = stringResource(R.string.invalid_email), // Error message to display
                color = Red,
                style = textRegularExtraSmall,
                modifier = Modifier
                    .padding(top = 7.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailTextFieldPreview() {
    var input1 by remember {
        mutableStateOf("")
    }
    var isError1 by remember {
        mutableStateOf(false)
    }

    var input2 by remember {
        mutableStateOf("")
    }
    var isError2 by remember {
        mutableStateOf(false)
    }

    BersihKanTheme {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Email",
                    style = subHeadlineLarge
                )
                EmailTextField(
                    input = input1,
                    isError = isError1,
                    onInputChange = { newValue ->
                        input1 = newValue
                        isError1 = !isEmailValid(input1)
                    },
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Email 2",
                    style = subHeadlineLarge
                )
                EmailTextField(
                    input = input2,
                    isError = isError2,
                    onInputChange = { newValue ->
                        input2 = newValue
                        isError2 = !isEmailValid(input2)
                    },
                )
            }
        }
    }
}

