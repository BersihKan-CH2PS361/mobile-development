package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.ui.components.DashedLine
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.Loblolly
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineExtraLarge
import com.example.bersihkan.ui.theme.textRegularSmall

@Composable
fun DiagramCard(
    plasticWeight: String,
    paperWeight: String,
    metalWeight: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(Shapes.medium)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp)
        ) {
            DiagramDesc(
                title = "Plastic",
                weight = "1.2",
                color = PacificBlue
            )
            DashedLine(
                strokeWidth = 4f,
                dashGap = 18f,
                dashWidth = 8f,
                vertical = true,
                color = Loblolly,
                modifier = Modifier
                    .width(50.dp)
            )
            DiagramDesc(
                title = "Paper",
                weight = "0.8",
                color = Java
            )
            DashedLine(
                strokeWidth = 4f,
                dashGap = 18f,
                dashWidth = 8f,
                vertical = true,
                color = Loblolly,
                modifier = Modifier
                    .width(50.dp)
            )
            DiagramDesc(
                title = "Metal",
                weight = "1.6",
                color = Cerulean
            )
        }
    }
}

@Composable
fun DiagramDesc(
    title: String,
    weight: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = subHeadlineExtraLarge,
            )
            Surface(
                shape = CircleShape,
                color = color,
                modifier = Modifier.size(12.dp)
            ) {

            }
        }
        Text(
            text = "$weight kg",
            style = textRegularSmall,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun DiagramCardPreview() {
    BersihKanTheme {
        DiagramCard(plasticWeight = "1.2", paperWeight = "0.8", metalWeight = "1.6")
    }
}