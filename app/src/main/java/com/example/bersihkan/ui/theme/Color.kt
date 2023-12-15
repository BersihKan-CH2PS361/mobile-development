package com.example.bersihkan.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val BlueLagoon = Color(0xFF047394)
val BlueChill = Color(0xFF088BA2)
val PacificBlue = Color(0xFF05A7BE)
val Java = Color(0xFF18C4B8)
val Cerulean = Color(0xFF01A7E4)
val BostonBlue = Color(0xFF3D90A9)
val PersianGreen = Color(0xFF00A79C)
val Botticelli = Color(0xFFCDE3EA)
val RockBlue = Color(0xFF9AC0CD)
val LightGrey = Color(0xFFECECEC)
val Grey = Color(0xFF7E949A)
val Mercury = Color(0xFFDDE2E4)
val Loblolly = Color(0xFFBEC9CC)
val Red = Color(0xFFC53E3E)
val BonJour = Color(0xFFE0E0E0)

fun gradient1(width: Float): Brush {
    return Brush.horizontalGradient(
        colors = listOf(BlueLagoon, BostonBlue),
        startX = 0f,
        endX = width
    )
}
fun gradient2(width: Float, height: Float): Brush {
    return Brush.linearGradient(
        colors = listOf(PacificBlue, PersianGreen),
        start = Offset(0f,0f),
        end = Offset((0.7f*width), (0.75f*height))
    )
}
fun gradient3(width: Float, height: Float): Brush {
    return Brush.linearGradient(
        colors = listOf(Botticelli, RockBlue),
        start = Offset(0.3f,0.3f),
        end = Offset(1f, 1f)
    )
}