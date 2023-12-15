package com.example.bersihkan.ui.components.modal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.bersihkan.R
import com.example.bersihkan.ui.components.buttons.SmallButton
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.headlineExtraSmall
import com.example.bersihkan.ui.theme.headlineMedium
import com.example.bersihkan.ui.theme.textMediumExtraSmall

@Composable
fun RegisterLoginDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = headlineMedium,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = message,
                style = textMediumExtraSmall
            )
        },
        confirmButton = {
            //FIXME: Change button to components button
            SmallButton(
                text = stringResource(id = R.string.ok_text),
                onClick = onDismiss,
                color = Java,
                isEnabled = true
            )
        },
        containerColor = Color.White,
        titleContentColor = Color.Black,
        textContentColor = Color.Black
    )
}