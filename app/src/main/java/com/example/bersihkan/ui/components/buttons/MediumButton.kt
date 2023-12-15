package com.example.bersihkan.ui.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.headlineSmall
import com.example.bersihkan.ui.theme.subHeadlineExtraLarge

@Composable
fun MediumButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onClick,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        modifier = modifier
            .heightIn(45.dp, 55.dp)
    ) {
        Text(
            text = text,
            style = subHeadlineExtraLarge.copy(
                color = Color.White
            ),
        )
    }

}

@Preview(showBackground = true)
@Composable
fun MediumButtonPrev() {
    BersihKanTheme {
        MediumButton(
            text = "Order",
            color = Java,
            onClick = { },
            isEnabled = true
        )
    }
}