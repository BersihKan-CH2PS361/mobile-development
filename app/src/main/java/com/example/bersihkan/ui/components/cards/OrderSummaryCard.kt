package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BonJour
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineExtraSmall
import com.example.bersihkan.ui.theme.subHeadlineLarge
import com.example.bersihkan.ui.theme.subHeadlineMedium
import com.example.bersihkan.ui.theme.textRegularTiny
import com.example.bersihkan.utils.WasteType

@Composable
fun OrderSummaryCard(
    wasteFee: Int,
    wasteType: WasteType,
    wasteQty: Int,
    modifier: Modifier = Modifier
) {

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(185.dp)
            .clip(Shapes.large)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            CardSummarySection(
                title = stringResource(id = R.string.waste_details),
                text1 = if(wasteType == WasteType.INITIAL || wasteQty <= 0) "-  • 0 kg" else "${wasteType.type}  • $wasteQty kg",
                text2 = "Rp.$wasteFee"
            )
            CardSummarySection(
                title = stringResource(R.string.delivery_title),
                text1 = stringResource(R.string.delivery_fee),
                text2 = "Rp.12000"
            )
            Divider(
                thickness = 1.dp,
                color = BonJour,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth(),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.subtotal),
                    style = subHeadlineMedium.copy(
                        color = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Rp.${wasteFee.plus(12000)}",
                    style = subHeadlineLarge.copy(
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun CardSummarySection(
    title: String,
    text1: String,
    text2: String,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = subHeadlineExtraSmall,
            color = Grey,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text1,
                style = textRegularTiny,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = text2,
                style = textRegularTiny,
                color = Color.Black
            )
        }
    }
    
}

@Preview
@Composable
fun OrderSummaryCardPreview() {
    BersihKanTheme {
        OrderSummaryCard(
            wasteFee = 0,
            wasteType = WasteType.INITIAL,
            wasteQty = 0,
        )
    }
}

