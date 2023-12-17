package com.example.bersihkan.ui.components.textFields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.subHeadlineExtraSmall
import com.example.bersihkan.ui.theme.textMediumMedium

@Composable
fun NotesInput(
    input: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var focusColor by remember {
        mutableStateOf(Color.Gray)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
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
            textStyle = textMediumMedium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            minLines = 5,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
                .onFocusChanged {
                    focusColor = if (it.isFocused) {
                        BlueLagoon
                    } else {
                        Color.Gray
                    }
                }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun NotesInputPreview() {
    var input by remember {
        mutableStateOf("")
    }
    BersihKanTheme {
        NotesInput(input = input, onInputChange = { newValue ->
            input = newValue
        })
    }
}