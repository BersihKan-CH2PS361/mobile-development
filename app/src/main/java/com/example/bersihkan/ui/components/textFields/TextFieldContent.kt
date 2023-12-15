package com.example.bersihkan.ui.components.textFields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.subHeadlineLarge

@Composable
fun TextFieldContent(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = subHeadlineLarge,
            color = Color.Black
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldContentPreview() {
    BersihKanTheme {
        TextFieldContent(
            title = stringResource(R.string.email),
            content = {
                EmailTextField(
                    input = "",
                    onInputChange = { newValue ->

                    },
                    isError = false
                )
            }
        )
    }
}