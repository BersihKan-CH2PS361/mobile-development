package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Botticelli
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineLarge
import com.example.bersihkan.ui.theme.textMediumExtraSmall
import com.example.bersihkan.ui.theme.textMediumSmall
import com.example.bersihkan.ui.theme.textRegularExtraSmall
import com.example.bersihkan.utils.WasteType

@Composable
fun OrderOngoingCard(
    wasteType: String,
    wasteQty: Int,
    totalFee: Int,
    modifier: Modifier = Modifier
) {

    Surface(
        color = Botticelli,
        shape = Shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(3.dp, Shapes.medium)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Ongoing Order",
                    style = subHeadlineLarge.copy(
                        color = Color.Black
                    ),
                    modifier = Modifier.offset(0.dp,(-3).dp)
                )
                Row {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_type),
                            contentDescription = "Waste Type",
                            tint = Cerulean,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if(wasteType != WasteType.INITIAL.type) wasteType else "-",
                            style = textRegularExtraSmall.copy(
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                            ),
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_weight),
                            contentDescription = "Weight",
                            tint = Java,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if(wasteQty > 0) "$wasteQty kg" else "0 kg",
                            style = textRegularExtraSmall.copy(
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                            ),
                        )
                    }
                }
            }
            Surface(
                shape = Shapes.extraSmall,
                color = Cerulean,
            ) {
                Text(
                    text = "Rp.$totalFee",
                    style = textMediumExtraSmall.copy(
                        color = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun OrderOngoingCardPreview() {
    BersihKanTheme {
        OrderOngoingCard(
            wasteType = WasteType.PLASTIC.type,
            wasteQty = 2,
            totalFee = 20000)
    }
}