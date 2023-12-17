package com.example.bersihkan.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.gradient3
import com.example.bersihkan.ui.theme.headlineLarge
import com.example.bersihkan.ui.theme.textMediumExtraSmall
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    qtyS: List<Float>,
    modifier: Modifier = Modifier
) {

    val colors = listOf(PacificBlue, Java, Cerulean)

    Canvas(modifier = modifier) {
        var startAngle = -90f
        val total = qtyS.sum()
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2
        val spaceBetweenArcs = 3.5f
        val path = Path()

        qtyS.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f - spaceBetweenArcs
            val startRadians = Math.toRadians(startAngle.toDouble())


            val startX = center.x + (radius * cos(startRadians)).toFloat()
            val startY = center.y + (radius * sin(startRadians)).toFloat()

            path.reset()
            path.moveTo(center.x, center.y)
            path.lineTo(startX, startY)
            path.arcTo(
                rect = size.toRect(),
                startAngleDegrees = startAngle,
                sweepAngleDegrees = sweepAngle,
                forceMoveTo = false
            )
            path.lineTo(center.x, center.y)
            path.close()

            drawPath(
                path = path,
                color = colors.getOrElse(index) { Color.Gray }
            )

            startAngle += sweepAngle + spaceBetweenArcs
        }
    }

}

@Composable
fun PieChartBox(
    qtyS: List<Float>,
    modifier: Modifier = Modifier
) {
    val total = qtyS.sum()
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        PieChart(
            qtyS = qtyS,
            modifier = Modifier.size(200.dp)
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(
                    gradient3(200f, 200f),
                    shape = CircleShape
                )
        )
        Surface(
            shape = CircleShape,
            color = Color.White,
            modifier = Modifier
                .size(120.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = total.toInt().toString(),
                        style = headlineLarge.copy(
                            color = Color.Black,
                            fontSize = 40.sp
                        ),
                        modifier = Modifier.height(55.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.kg_of_waste),
                        style = textMediumExtraSmall.copy(
                            color = Color.Black
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PieChartBoxPreview() {
    BersihKanTheme {
        PieChartBox(qtyS = listOf(2f,3f,1f))
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun PieChartPreview() {
    BersihKanTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(gradient3(200f, 200f))
        ) {
            PieChart(
                qtyS = listOf(1f,5f,3f),
                modifier = Modifier.size(200.dp)
            )
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(
                        brush = gradient3(200f, 200f),
                        shape = CircleShape
                    )
            )
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier
                    .size(120.dp)
            ) {

            }
        }
    }
}

@Composable
fun ClippedPieChart(
    qtyS: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val spaceBetweenArcs = 3f
    val holeRadius = 50f // Adjust the size of the hole

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        clipPath(path = Path()) {
            // Outer circle
            val outerCircle = Path().apply {
                addOval(
                    Rect(
                        left = center.x - radius,
                        top = center.y - radius,
                        right = center.x + radius,
                        bottom = center.y + radius
                    )
                )
            }

            // Inner circle (to create a hole)
            val innerCircle = Path().apply {
                addOval(
                    Rect(
                        left = center.x - holeRadius,
                        top = center.y - holeRadius,
                        right = center.x + holeRadius,
                        bottom = center.y + holeRadius
                    )
                )
            }

            // Subtract the inner circle from the outer circle to create a hole
            outerCircle.op(innerCircle, Path(), PathOperation.Difference)
        }

        val path = Path()

        var startAngle = -90f
        val total = qtyS.sum()

        qtyS.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f - spaceBetweenArcs
            val startRadians = Math.toRadians(startAngle.toDouble())

            val startX = center.x + (radius * cos(startRadians)).toFloat()
            val startY = center.y + (radius * sin(startRadians)).toFloat()

            path.reset()
            path.moveTo(center.x, center.y)
            path.lineTo(startX, startY)
            path.arcTo(
                rect = size.toRect(),
                startAngleDegrees = startAngle,
                sweepAngleDegrees = sweepAngle,
                forceMoveTo = false
            )
            path.lineTo(center.x, center.y)
            path.close()

            drawPath(
                path = path,
                color = colors.getOrElse(index) { Color.Gray }
            )

            startAngle += sweepAngle + spaceBetweenArcs
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClippedPieChartPreview() {
    ClippedPieChart(
        qtyS = listOf(30f, 20f, 50f),
        colors = listOf(Color.Blue, Color.Green, Color.Red),
        modifier = Modifier.size(200.dp)
    )
}