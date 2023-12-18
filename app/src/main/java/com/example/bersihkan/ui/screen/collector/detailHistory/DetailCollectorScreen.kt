package com.example.bersihkan.ui.screen.collector.detailHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.helper.convertToDate
import com.example.bersihkan.ui.components.cards.DeliveryCustCard
import com.example.bersihkan.ui.components.cards.HistoryCard
import com.example.bersihkan.ui.components.cards.OrderSummaryCard
import com.example.bersihkan.ui.components.section.HomeSection
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.gradient2
import com.example.bersihkan.ui.theme.gradient3
import com.example.bersihkan.ui.theme.textMediumMedium
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.data.remote.response.DetailOrderAll
import com.example.bersihkan.data.remote.response.DetailOrderCollectorAll
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.ui.components.cards.CollectorInfoCard
import com.example.bersihkan.ui.components.cards.DeliveryCollectorCard
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.kekkomiapp.ui.common.UiState

@Composable
fun DetailCollectorScreen(
    orderId: Int,
    navigateToBack: () -> Unit,
    viewModel: DetailCollectorViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    LaunchedEffect(
        key1 = viewModel,
        block = {
            viewModel.getDetailHistoryById(id = orderId)
    })

    viewModel.histories.collectAsState().value.let { history ->
        when(history){
            is UiState.Initial -> {}
            is UiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        color = Grey,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            is UiState.Success -> {
                DetailCollectorContent(
                    history = history.data,
                    navigateToBack = navigateToBack
                )
            }
            is UiState.Error -> {
                DetailCollectorContent(
                    history = DetailOrderCollectorAll(),
                    navigateToBack = navigateToBack,
                    isError = true,
                    textError = history.errorMsg
                )
            }
        }
    }

}

@Composable
fun DetailCollectorContent(
    history: DetailOrderCollectorAll,
    navigateToBack: () -> Unit,
    isError: Boolean = false,
    textError: String = "",
    modifier: Modifier = Modifier
) {
    val detailOrder = history.detailOrderResponse

    Box(modifier = modifier
        .background(gradient3(800f, 1600f)))
    {

        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ){
            item {
                TopBar(
                    text = stringResource(R.string.detail_history),
                    onBackClick = navigateToBack,
                    enableOnBack = true
                )
            }
            if(isError){
                item{
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillParentMaxSize()
                    ) {
                        Text(
                            text = textError,
                            style = textMediumMedium,
                            color = Grey
                        )
                    }
                }
            } else {
                item {
                    DeliveryCollectorCard(
                        pickup = history.addressFromLatLng.toString(),
                        wasteType = detailOrder?.wasteType.toString(),
                        wasteQty = detailOrder?.wasteQty ?: 0,
                        totalFee = detailOrder?.subtotalFee ?: 0,
                        date = convertToDate(detailOrder?.orderDatetime ?: ""),
                        notes = detailOrder?.userNotes.toString(),
                        userName = detailOrder?.userName.toString(),
                        userPhone = detailOrder?.userPhone.toString(),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
                item {
                    HomeSection(
                        title = stringResource(R.string.detail_fee),
                        content = {
                            if(detailOrder?.wasteType != null && detailOrder.wasteQty != null){
                                OrderSummaryCard(
                                    wasteFee = 4000,
                                    wasteType = detailOrder.wasteType,
                                    wasteQty = detailOrder.wasteQty,
                                )
                            }
                        },
                        navigateToStatistic = { },
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                    )
                }
            }
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun DetailContentPreview() {
    BersihKanTheme {
        DetailCollectorContent(
            history = DataDummy.detailOrderCollectorAll,
            navigateToBack = { /*TODO*/ }
        )
    }
}