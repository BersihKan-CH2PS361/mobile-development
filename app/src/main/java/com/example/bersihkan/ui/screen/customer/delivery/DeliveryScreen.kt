package com.example.bersihkan.ui.screen.customer.delivery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.data.remote.response.DetailOrderAll
import com.example.bersihkan.helper.convertToDate
import com.example.bersihkan.helper.findOrderStatus
import com.example.bersihkan.ui.components.cards.CollectorInfoCard
import com.example.bersihkan.ui.components.cards.DeliveryCustCard
import com.example.bersihkan.ui.components.cards.OrderSummaryCard
import com.example.bersihkan.ui.components.section.HomeSection
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.gradient2
import com.example.bersihkan.utils.OrderStatus
import com.example.bersihkan.utils.WasteType

@Composable
fun DeliveryScreen(
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {

}

@Composable
fun DeliveryContent(
    orderData: DetailOrderAll,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val detailOrder = orderData.detailOrderResponse
    val detailCollector = orderData.detailCollectorResponse
    LazyColumn(
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.background(gradient2(800f,1600f))
    ){
        item {
            TopBar(
                text = stringResource(id = R.string.delivery_title),
                onBackClick = navigateToBack,
                enableOnBack = true,
                color = Color.White
            )
        }
        item {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                DeliveryPicture(
                    status = findOrderStatus(detailOrder?.orderStatus.toString()),
                    modifier = Modifier.size(250.dp)
                )
            }
        }
        item {
            DeliveryCustCard(
                pickup = orderData.addressFromLatLng.toString(),
                wasteType = detailOrder?.wasteType.toString(),
                wasteQty = detailOrder?.wasteQty ?: 0,
                totalFee = detailOrder?.subtotalFee ?: 0,
                date = convertToDate(detailOrder?.orderDatetime.toString()),
                orderStatus = detailOrder?.orderStatus.toString(),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
        item {
            HomeSection(
                title = stringResource(R.string.detail_fee),
                content = {
                    if(detailOrder?.wasteType != null && detailOrder?.wasteQty != null){
                        OrderSummaryCard(
                            wasteFee = 4000,
                            wasteType = detailOrder?.wasteType,
                            wasteQty = detailOrder?.wasteQty
                        )
                    }
                },
                color = Color.White,
                navigateToStatistic = { },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
        item {
            HomeSection(
                title = stringResource(R.string.collector_info),
                content = {
                    CollectorInfoCard(
                        name = detailCollector?.name.toString(),
                        phoneNumber = detailCollector?.phone.toString(),
                        wasteHouse = detailCollector?.facilityName.toString()
                    )
                },
                color = Color.White,
                navigateToStatistic = { },
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            )
        }
    }

}

@Composable
fun DeliveryPicture(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (pict, desc) = when (status) {
        OrderStatus.INITIAL -> Pair(0, OrderStatus.INITIAL.status)
        OrderStatus.PICK_UP -> Pair(R.drawable.ic_pick_up, OrderStatus.PICK_UP.status)
        OrderStatus.ARRIVED -> Pair(R.drawable.ic_arrived, OrderStatus.ARRIVED.status)
        OrderStatus.DELIVERING -> Pair(R.drawable.ic_delivering, OrderStatus.DELIVERING.status)
        OrderStatus.DELIVERED -> Pair(R.drawable.ic_pick_up, OrderStatus.DELIVERED.status)
    }

    Image(
        painter = painterResource(id = pict),
        contentDescription = desc,
        modifier = modifier
    )
}

@Preview
@Composable
fun DeliveryContentPreview() {
    BersihKanTheme {
        DeliveryContent(
            orderData = DataDummy.detailOrderAll2,
            navigateToBack = { /*TODO*/ })
    }
}