package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bersihkan.R
import com.example.bersihkan.ui.components.DashedLine
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.fontFamily
import com.example.bersihkan.ui.theme.subHeadlineExtraSmall
import com.example.bersihkan.ui.theme.textRegularExtraSmall

@Composable
fun RecentTransactionsCard(
    date: String,
    address: String,
    type: String,
    weight: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Transparent,
        modifier = modifier
            .border(BorderStroke(2.dp, BlueLagoon), Shapes.medium)
            .fillMaxWidth()
            .height(130.dp)
            .clip(Shapes.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = date,
                style = subHeadlineExtraSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            DashedLine(
                color = BlueLagoon,
                strokeWidth = 4f,
                dashGap = 18f,
                dashWidth = 8f,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = address,
                    style = textRegularExtraSmall,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconAndText(
                        icon = R.drawable.ic_type,
                        iconColor = PacificBlue,
                        text = "$type"
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconAndText(
                        icon = R.drawable.ic_weight,
                        iconColor = Java,
                        text = "$weight kg"
                    )
                }
            }
        }
    }
}

@Composable
fun IconAndText(
    icon: Int,
    iconColor: Color,
    text: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecentTransactionsCardPreview() {
    BersihKanTheme {
        RecentTransactionsCard(
            date = "5 January 2023",
            address = "Jl. Ir. Juanda No. 50",
            type = "Plastic",
            weight = "3.5"
        )
    }
}