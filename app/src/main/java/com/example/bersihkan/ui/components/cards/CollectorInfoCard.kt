package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineTiny
import com.example.bersihkan.ui.theme.textRegularSmall

@Composable
fun CollectorInfoCard(
    name: String,
    phoneNumber: String,
    wasteHouse: String,
    modifier: Modifier = Modifier
) {

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(125.dp)
            .clip(Shapes.large)
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_collector_profile),
                    contentDescription = "Collector Profile Photo",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = name,
                    style = textRegularSmall.copy(
                        color = Color.Black
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_telephone),
                        contentDescription = "Collector's contact",
                        tint = Java,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = name,
                        style = subHeadlineTiny.copy(
                            color = PacificBlue,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = wasteHouse,
                        style = subHeadlineTiny.copy(
                            color = Cerulean,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_recycle_house),
                        contentDescription = "Collector's contact",
                        tint = Cerulean,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun CollectorInfoCardPreview() {
    BersihKanTheme {
        CollectorInfoCard(
            name = "Edward Choi",
            phoneNumber = "081234567890",
            wasteHouse = "Rumah Cempaka 2",
            )
    }
}