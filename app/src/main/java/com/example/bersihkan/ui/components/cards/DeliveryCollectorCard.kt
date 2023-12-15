package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.helper.convertToDate
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueChill
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.textRegularExtraSmall
import com.example.bersihkan.ui.theme.textRegularTiny
import com.example.bersihkan.utils.WasteType

@Composable
fun DeliveryCollectorCard(
    pickup: String,
    wasteType: String,
    wasteQty: Int,
    totalFee: Int,
    date: String,
    notes: String,
    userName: String,
    userPhone: String,
    modifier: Modifier = Modifier
) {

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(550.dp)
            .clip(Shapes.large)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            DeliveryCardSection(
                date = date,
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user_filled),
                                contentDescription = "User\'s Name",
                                tint = BlueChill,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = userName,
                                style = textRegularExtraSmall.copy(
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                ),
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.offset((-12).dp, 0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_telephone),
                                contentDescription = "User\'s Contact",
                                tint = BlueChill,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = userPhone,
                                style = textRegularExtraSmall.copy(
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                ),
                            )
                        }
                    }
                }
            )
            CardSection(
                title = stringResource(R.string.pick_up),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = "Location",
                            tint = PacificBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = pickup,
                            style = textRegularExtraSmall.copy(
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            )
            CardSection(
                title = stringResource(R.string.waste_details),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                text = if(wasteQty > 0) wasteQty.toString() else "0",
                                style = textRegularExtraSmall.copy(
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                ),
                            )
                        }
                    }
                }
            )
            CardSection(
                title = stringResource(R.string.notes),
                content = {
                    Text(
                        text = if(notes.isNotEmpty() || notes != "") notes else "-",
                        style = textRegularExtraSmall.copy(
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                        ),
                        minLines = 5,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    )
                },
            )
            CardSection(
                title = stringResource(R.string.total_fee),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_money_bill),
                                contentDescription = "Location",
                                tint = BlueChill,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Rp.$totalFee",
                                style = textRegularExtraSmall.copy(
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                ),
                            )
                        }
                        Text(
                            text = stringResource(R.string.cash),
                            style = textRegularTiny.copy(
                                color = Grey,
                                textAlign = TextAlign.Start,
                            ),
                            modifier = Modifier.offset((-10).dp, 0.dp)
                        )
                    }
                },
                showDivider = false
            )
        }
    }
}

@Preview
@Composable
fun DeliveryCollectorCardPreview() {
    BersihKanTheme {
        val data = DataDummy.detailOrderResponse[2]
        DeliveryCollectorCard(
            pickup = "Jl Ir. Juanda No.50",
            wasteType = data.wasteType!!,
            wasteQty = data.wasteQty!!,
            totalFee = data.subtotalFee!!,
            date = convertToDate(data.orderDatetime!!),
            userName = "Elizabeth",
            userPhone = "081234567890",
            notes = "Lorem ipsum dolor sit amet consectetur. Lorem pharetra at viverra nibh sed quisque non odio faucibus. Ut sit lobortis purus pellentesque. Ut bibendum vel sed nibh morbi. Habitant integer arcu eget turpis leo amet arcu semper. Aliquet venenatis vulputate vitae nisl iaculis sit. Morbi imperdiet ipsum et magna eget venenatis eget diam. Quis eu posuere porta nibh sem at turpis vel elementum. In augue morbi purus diam massa. Dignissim tempus turpis massa vel sed leo in donec."
        )
    }
}