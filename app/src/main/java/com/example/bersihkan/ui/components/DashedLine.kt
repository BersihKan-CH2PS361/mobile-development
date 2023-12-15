package com.example.bersihkan.ui.components

import android.graphics.DashPathEffect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.ui.theme.BersihKanTheme

@Composable
fun DashedLine(
    strokeWidth: Float = 2f,
    dashWidth: Float = 8f,
    dashGap: Float = 4f,
    color: Color = Color.Black,
    vertical: Boolean = false,
    lineLength: Float? = null,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(strokeWidth.dp),
        onDraw = {
            var mLineLength = lineLength ?: this.size.width

            rotate(if (vertical) 90f else 0f){
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(mLineLength, 0f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DashedLinePreview() {
    BersihKanTheme {
        DashedLine(modifier = Modifier.fillMaxWidth())
    }
}