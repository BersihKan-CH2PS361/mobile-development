package com.example.bersihkan.ui.components.cards

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueChill
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Botticelli
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineTiny
import com.example.bersihkan.ui.theme.textRegularMedium

@Composable
fun HistoryCard(
    date: String,
    orderId: String,
    type: String,
    weight: String,
    dropPoint: String,
    total_fee: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Botticelli,
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(Shapes.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = date,
                    style = subHeadlineTiny,
                    color = Grey,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "Order No.$orderId",
                    style = subHeadlineTiny,
                    textAlign = TextAlign.End,
                    color = BlueLagoon,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_type),
                        contentDescription = null,
                        tint = PacificBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = type,
                        style = textRegularMedium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Text(
                        text = "$weight kg",
                        style = textRegularMedium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_weight),
                        contentDescription = null,
                        tint = Java,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dropPoint,
                    style = subHeadlineTiny,
                    color = BlueChill,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = Cerulean,
                    contentColor = Color.White,
                    modifier = Modifier
                        .clip(Shapes.extraSmall)
                ) {
                    Text(
                        text = "Rp.$total_fee",
                        style = subHeadlineTiny,
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryCardPreview() {
    BersihKanTheme {
        HistoryCard(
            date = "Sunday, November 26 2023",
            orderId = "10",
            type = "Plastic",
            weight = "1.2",
            dropPoint = "Rumah Plastik Cempaka",
            total_fee = "20000"
        )
    }
}