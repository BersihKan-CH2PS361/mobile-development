package com.example.bersihkan.ui.screen.collector.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.helper.convertToDate
import com.example.bersihkan.ui.components.cards.HistoryCard
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.screen.customer.history.HistoryContent
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.textMediumMedium
import com.example.kekkomiapp.ui.common.UiState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bersihkan.ui.screen.ViewModelFactory

@Composable
fun HistoryCollectorScreen(
    navigateToDetail: (Int) -> Unit,
    viewModel: HistoryCollectorViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    LaunchedEffect(
        key1 = viewModel,
        block = {
            viewModel.getDetailHistoryCollector()
        })
    viewModel.histories.collectAsState().value.let { data ->
        when(data){
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
                if(data.data.isNotEmpty()){
                    HistoryCollectorContent(
                        histories = data.data,
                        navigateToDetail = navigateToDetail
                    )
                } else {
                    HistoryCollectorContent(
                        histories = emptyList(),
                        navigateToDetail = { },
                        isError = true,
                        textError = stringResource(id = R.string.no_data_found)
                    )
                }
            }
            is UiState.Error -> {
                HistoryCollectorContent(
                    histories = emptyList(),
                    navigateToDetail = { },
                    isError = true,
                    textError = data.errorMsg
                )
            }
        }
    }

}

@Composable
fun HistoryCollectorContent(
    histories: List<DetailOrderResponse>,
    navigateToDetail: (Int) -> Unit,
    isError: Boolean = false,
    textError: String = "",
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        state = rememberLazyListState(),
        modifier = modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {

        item{
            TopBar(
                text = stringResource(id = R.string.menu_history),
                onBackClick = {}
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
            items(histories,key = { it.orderId!! }){ history ->
                HistoryCard(
                    date = convertToDate(history.orderDatetime.toString()),
                    type = history.wasteType.toString(),
                    weight = history.wasteQty.toString(),
                    dropPoint = history.facilityName.toString(),
                    totalFee = history.subtotalFee.toString(),
                    modifier = Modifier.clickable {
                        history.orderId?.let { navigateToDetail(it) }
                    }
                )
            }
        }

    }

}