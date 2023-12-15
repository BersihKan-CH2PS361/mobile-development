package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.helper.convertToDate
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueChill
import com.example.bersihkan.ui.theme.BonJour
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineTiny
import com.example.bersihkan.ui.theme.textRegularExtraSmall
import com.example.bersihkan.ui.theme.textRegularSmall
import com.example.bersihkan.ui.theme.textRegularTiny
import com.example.bersihkan.utils.OrderStatus
import com.example.bersihkan.utils.WasteType

@Composable
fun DeliveryCustCard(
    pickup: String,
    wasteType: String,
    wasteQty: Int,
    totalFee: Int,
    date: String,
    orderStatus: String,
    modifier: Modifier = Modifier
) {

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(465.dp)
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
                title = stringResource(R.string.track_delivery),
                content = {
                    DeliveryTracking(orderStatus = orderStatus)
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

@Composable
fun DeliveryCardSection(
    date: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = date,
            style = subHeadlineTiny.copy(
                color = PacificBlue
            )
        )
        content()
        Divider(
            thickness = 1.dp,
            color = BonJour,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    
}

@Composable
fun DeliveryTracking(
    orderStatus: String,
    modifier: Modifier = Modifier
) {

    val status: OrderStatus = when(orderStatus){
        OrderStatus.PICK_UP.status -> OrderStatus.PICK_UP
        OrderStatus.ARRIVED.status -> OrderStatus.ARRIVED
        OrderStatus.DELIVERING.status -> OrderStatus.DELIVERING
        OrderStatus.DELIVERED.status -> OrderStatus.DELIVERED
        else -> {OrderStatus.INITIAL}
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ColumnIcons(ongoingStatus = status, modifier = Modifier.height(155.dp))
        ColumnStatus(ongoingStatus = status, modifier = Modifier.height(155.dp))
    }

}

@Composable
fun ColumnIcons(
    ongoingStatus: OrderStatus,
    modifier: Modifier = Modifier
) {
    val orderStatus = OrderStatus.values()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        orderStatus.forEach { status ->
            if(status == ongoingStatus){
                Icon(
                    painter = painterResource(id = R.drawable.ic_status_ongoing),
                    contentDescription = null,
                    tint = Java,
                    modifier = Modifier.size(20.dp)
                )
            } else if(status < ongoingStatus){
                Icon(
                    painter = painterResource(id = R.drawable.ic_status_done),
                    contentDescription = null,
                    tint = BlueChill,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_status_loading),
                    contentDescription = null,
                    tint = BlueChill,
                    modifier = Modifier.size(16.dp)
                )
            }
            if(status != OrderStatus.DELIVERED){
                Icon(
                    painter = painterResource(id = R.drawable.ic_vertical_dashed_line),
                    contentDescription = null,
                    tint = BlueChill,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun ColumnStatus(
    ongoingStatus: OrderStatus,
    modifier: Modifier = Modifier
) {
    val orderStatus = OrderStatus.values()
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        orderStatus.forEach { status ->
            if(status == ongoingStatus){
                Text(
                    text = stringResource(id = status.text),
                    style = textRegularSmall.copy(
                        color = Color.Black
                    )
                )
            } else {
                Text(
                    text = stringResource(id = status.text),
                    style = textRegularTiny.copy(
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeliveryTrackingPreview() {
    BersihKanTheme {
        DeliveryTracking(OrderStatus.ARRIVED.status)
    }
}

@Preview
@Composable
fun DeliveryCustCardPreview() {
    BersihKanTheme {
        val data = DataDummy.detailOrderResponse[2]
        DeliveryCustCard(
            pickup = "Jl Ir. Juanda No.50",
            wasteType = data.wasteType!!,
            wasteQty = data.wasteQty!!,
            totalFee = data.subtotalFee!!,
            orderStatus = data.orderStatus!!,
            date = convertToDate(data.orderDatetime!!).toString()
        )
    }
}