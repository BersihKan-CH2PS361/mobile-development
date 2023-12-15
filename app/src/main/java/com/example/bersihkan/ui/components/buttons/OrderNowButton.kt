package com.example.bersihkan.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.fontFamily

@Composable
fun OrderNowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .padding(horizontal = 4.dp)
    ) {
        val shadowGradient = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.1f)),
            startY = 0f,
            endY = 40f
        )

        Box(
            modifier = Modifier
                .graphicsLayer(shadowElevation = 5f)
                .background(
                    brush = shadowGradient
                )
        )

        Button(
            onClick = onClick,
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = BlueLagoon
            ),
            modifier = modifier
                .height(36.dp)
        ) {
            Text(
                text = stringResource(R.string.order_now),
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderNowButtonPreview() {
    BersihKanTheme {
        OrderNowButton(onClick = { })
    }
}