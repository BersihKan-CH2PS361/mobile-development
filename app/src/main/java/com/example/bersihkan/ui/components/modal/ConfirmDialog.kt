package com.example.bersihkan.ui.components.modal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.bersihkan.R
import com.example.bersihkan.ui.components.buttons.SmallButton
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.Red
import com.example.bersihkan.ui.theme.headlineMedium
import com.example.bersihkan.ui.theme.textMediumExtraSmall

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onPositive: () -> Unit,
    onDismiss: () -> Unit,
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
                text = stringResource(id = R.string.yes),
                onClick = onPositive,
                color = Java,
                isEnabled = true
            )
        },
        dismissButton = {
            SmallButton(
                text = stringResource(id = R.string.no),
                onClick = onDismiss,
                color = Red,
                isEnabled = true
            )
        },
        containerColor = Color.White,
        titleContentColor = Color.Black,
        textContentColor = Color.Black
    )

}