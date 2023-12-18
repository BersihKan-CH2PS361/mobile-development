package com.example.bersihkan.ui.screen.customer.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.helper.WasteTypeTotal
import com.example.bersihkan.helper.calculateStatisticsCount
import com.example.bersihkan.helper.calculateStatisticsTotals
import com.example.bersihkan.helper.calculateTotalWasteByType
import com.example.bersihkan.ui.components.PieChartBox
import com.example.bersihkan.ui.components.cards.DiagramCard
import com.example.bersihkan.ui.components.cards.StatisticsItem
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.gradient1
import com.example.bersihkan.ui.theme.gradient2
import com.example.bersihkan.ui.theme.gradient3
import com.example.bersihkan.utils.Statistics
import com.example.bersihkan.utils.WasteType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.textMediumMedium
import com.example.kekkomiapp.ui.common.UiState

@Composable
fun StatisticsScreen(
    navigateToBack: () -> Unit,
    viewModel: StatisticsViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    LaunchedEffect(key1 = viewModel, block = {
        viewModel.getDetailHistory()
    })

    viewModel.histories.collectAsState().value.let { data ->
        when(data) {
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
                val history = data.data
                StatisticContent(
                    waste = calculateTotalWasteByType(history),
                    statistics = calculateStatisticsTotals(history, LocalContext.current),
                    data = calculateStatisticsCount(history, LocalContext.current),
                    navigateToBack = navigateToBack,
                    modifier = modifier
                )
            }
            is UiState.Error -> {
                Column(modifier) {
                    TopBar(
                        text = stringResource(id = R.string.statistics),
                        onBackClick = navigateToBack,
                        enableOnBack = true
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = data.errorMsg,
                            style = textMediumMedium,
                            color = Grey
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun StatisticContent(
    waste: List<WasteTypeTotal>,
    statistics: List<Statistics>,
    data: List<Statistics>,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val plasticQty = waste.find { it.wasteType == WasteType.PLASTIC }?.totalQuantity ?: 0
    val paperQty = waste.find { it.wasteType == WasteType.PAPER }?.totalQuantity ?: 0
    val rubberQty = waste.find { it.wasteType == WasteType.RUBBER }?.totalQuantity ?: 0
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        4.dp,
                        Shapes.extraLarge.copy(
                            bottomStart = CornerSize(40.dp),
                            bottomEnd = CornerSize(40.dp),
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp)
                        )
                    )
                    .background(
                        gradient3(500f, 1000f),
                        shape = Shapes.extraLarge.copy(
                            bottomStart = CornerSize(40.dp),
                            bottomEnd = CornerSize(40.dp),
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp)
                        )
                    )
            ) {
                Column {
                    TopBar(
                        text = stringResource(id = R.string.statistics),
                        onBackClick = navigateToBack,
                        enableOnBack = true
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(vertical = 40.dp)
                            .fillMaxWidth()
                    ) {
                        PieChartBox(
                            qtyS = listOf(
                                plasticQty.toFloat(),
                                paperQty.toFloat(),
                                rubberQty.toFloat()
                            )
                        )
                    }
                    DiagramCard(
                        plasticWeight = plasticQty.toString(),
                        paperWeight = paperQty.toString(),
                        rubberWeight = rubberQty.toString(),
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                    )
                }
            }
        }
        item {
            Column {
                StatisticsItem(
                    icon = R.drawable.ic_order_made,
                    count = data[0].qty,
                    title = data[0].item
                )
                StatisticsItem(
                    icon = R.drawable.ic_location,
                    count = data[1].qty,
                    title = data[1].item
                )
                StatisticsItem(
                    icon = R.drawable.ic_recycle_house_2,
                    count = data[2].qty,
                    title = data[2].item
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun StatisticContentPreview() {
    BersihKanTheme {
        val statistics =
            calculateStatisticsTotals(DataDummy.detailOrderResponse, LocalContext.current)
        val waste = calculateTotalWasteByType(DataDummy.detailOrderResponse)
        val data = calculateStatisticsCount(DataDummy.detailOrderResponse, LocalContext.current)
        StatisticContent(
            waste = waste,
            statistics = statistics,
            data = data,
            navigateToBack = { })
    }
}