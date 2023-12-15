package com.example.bersihkan.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineExtraSmall
import com.example.bersihkan.ui.theme.textMediumMedium

@Composable
fun QuantityInput(
    input: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var focusColor by remember {
        mutableStateOf(Color.Gray)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.weight),
            style = subHeadlineExtraSmall,
            color = Grey
        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(108.dp) // Set specific width
                    .height(44.dp) // Set specific height
                    .border(
                        width = 2.dp,
                        color = focusColor,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clip(MaterialTheme.shapes.medium)
            ) {
                BasicTextField(
                    value = input,
                    onValueChange = onInputChange,
                    textStyle = textMediumMedium.copy(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ) // Adjust padding for text only
                        .align(Alignment.Center)
                        .onFocusChanged {
                            focusColor = if (it.isFocused) {
                                BlueLagoon // Change to your focused color
                            } else {
                                Color.Gray
                            }
                        }
                )
                }
            Text(
                text = "kg",
                style = textMediumMedium,
                color = Color.Black,
                modifier = Modifier.padding(start = 12.dp)
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun QuantityInputPreview() {
    var input by remember {
        mutableStateOf("")
    }
    BersihKanTheme {
        QuantityInput(input = input, onInputChange = { newValue ->
            input = newValue
        })
    }
}