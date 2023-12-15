package com.example.bersihkan.ui.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.example.bersihkan.ui.theme.headlineSmall

@Composable
fun LargeButton(
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
            .fillMaxWidth()
            .heightIn(60.dp, 70.dp)
    ) {
        Text(
            text = text,
            style = headlineSmall.copy(
                color = Color.White,
                fontSize = 20.sp
            ),
        )
    }

}

@Preview(showBackground = true)
@Composable
fun LargeButtonPrev() {
    BersihKanTheme {
        LargeButton(
            text = "Order",
            color = BlueLagoon,
            onClick = { },
            isEnabled = true
        )
    }
}