package com.example.bersihkan.ui.screen.collector.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.bersihkan.R
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.helper.WasteTypeTotal
import com.example.bersihkan.helper.calculateStatisticsTotals
import com.example.bersihkan.helper.calculateTotalWasteByType
import com.example.bersihkan.helper.convertToDate
import com.example.bersihkan.ui.components.cards.CurrentBalanceCard
import com.example.bersihkan.ui.components.cards.OrderNowCard
import com.example.bersihkan.ui.components.cards.OrderOngoingCard
import com.example.bersihkan.ui.components.cards.RecentTransactionsCard
import com.example.bersihkan.ui.components.section.HomeSection
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.bersihkan.ui.screen.customer.home.Banner
import com.example.bersihkan.ui.screen.customer.home.FunFactsCardRow
import com.example.bersihkan.ui.screen.customer.home.StatisticCardRow
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.headlineSmall
import com.example.bersihkan.ui.theme.textMediumMedium
import com.example.bersihkan.utils.Statistics
import com.example.kekkomiapp.ui.common.UiState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.bersihkan.notification.NotificationWorker
import com.example.bersihkan.utils.UserRole

@Composable
fun HomeCollectorScreen(
    navigateToDetail: (Int) -> Unit,
    navigateToDelivery: (Int) -> Unit,
    viewModel: HomeCollectorViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    workManager: WorkManager = WorkManager.getInstance(LocalContext.current),
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    viewModel.getSession()

    LaunchedEffect(key1 = viewModel, block = {
        viewModel.refreshData()
    })

    val isPermissionNotificationGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
            } else {
                Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

    if (isPermissionNotificationGranted) {

    } else {
        DisposableEffect(Unit) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            onDispose { }
        }
    }

    val name = viewModel.userModel.collectAsState().value
    val histories = viewModel.histories.collectAsState().value
    val ongoingOrder = viewModel.ongoingOrder.collectAsState().value

    HomeCollectorContent(
        name = name.name,
        profile = R.drawable.ic_collector_profile,
        histories = histories,
        ongoingOrder = ongoingOrder,
        navigateToDetail = navigateToDetail,
        navigateToDelivery = navigateToDelivery
    )

    viewModel.notification.collectAsState().value.let {  notification ->
        notification.getContentIfNotHandled().let { isShowed ->
            if(isShowed == true){
                val dataNotif = Data.Builder()
                    .putString(NotificationWorker.EXTRA_USER, UserRole.COLLECTOR.role)
                    .putString(NotificationWorker.EXTRA_STATUS, viewModel.orderStatus.value.status)
                    .build()
                val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                    .setInputData(dataNotif)
                    .build()
                workManager.enqueue(oneTimeWorkRequest)
            }
        }
    }

}

@Composable
fun HomeCollectorContent(
    name: String,
    profile: Int,
    histories: UiState<List<DetailOrderResponse>>,
    ongoingOrder: UiState<DetailOrderResponse>,
    navigateToDetail: (Int) -> Unit,
    navigateToDelivery: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Box {
        histories.let { data ->
            when (data) {
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
                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = modifier
                            .background(Color.White)
                    ) {
                        item {
                            Box {
                                Banner(
                                    name = name,
                                    profile = profile
                                )
                                CurrentBalanceCard(
                                    currentBalance = data.data.sumOf {
                                        it.subtotalFee ?: 0
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                        .offset(0.dp, 150.dp)
                                )
                            }
                        }
                        item {
                            val wasteTypeTotal = calculateTotalWasteByType(data.data)
                            val statistics = wasteTypeTotal.map { waste ->
                                Statistics(
                                    qty = waste.totalQuantity,
                                    item = stringResource(R.string.kg),
                                    desc = stringResource(
                                        R.string.of,
                                        waste.wasteType.type.lowercase()
                                    ),
                                )
                            }
                            HomeSection(
                                title = stringResource(R.string.waste_statistics),
                                content = {
                                    if (statistics.isNotEmpty()) {
                                        StatisticCardRow(
                                            statistics = statistics,
                                        )
                                    } else {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(175.dp)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.no_data_found),
                                                style = textMediumMedium,
                                                color = Grey
                                            )
                                        }
                                    }
                                },
                                navigateToStatistic = { },
                                modifier = Modifier.padding(top = 50.dp, start = 20.dp)
                            )
                        }
                        item {
                            Text(
                                text = stringResource(R.string.recent_transactions),
                                style = headlineSmall,
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 30.dp)
                            )
                        }
                        val hist = data.data
                        if (hist.isNotEmpty()) {
                            items(hist, key = { it.orderId!! }) { history ->
                                if (history == hist.last()) {
                                    RecentTransactionsCard(
                                        date = convertToDate(
                                            history.orderDatetime ?: ""
                                        ).toString(),
                                        address = history.pickupDatetime ?: "",
                                        type = history.wasteType ?: "",
                                        weight = history.wasteQty.toString(),
                                        modifier = Modifier
                                            .padding(
                                                start = 20.dp,
                                                end = 20.dp,
                                                top = 10.dp,
                                                bottom = 30.dp
                                            )
                                            .clickable { navigateToDetail(history.orderId ?: 0) }
                                    )
                                } else {
                                    RecentTransactionsCard(
                                        date = convertToDate(
                                            history.orderDatetime ?: ""
                                        ),
                                        address = history.facilityName.toString(),
                                        type = history.wasteType ?: "",
                                        weight = history.wasteQty.toString(),
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 20.dp,
                                                vertical = 10.dp
                                            )
                                            .clickable { navigateToDetail(history.orderId ?: 0) }
                                    )
                                }
                            }
                        } else {
                            item {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.no_data_found),
                                        style = textMediumMedium,
                                        color = Grey
                                    )
                                }
                            }
                        }
                    }
                }

                is UiState.Error -> {
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
        ongoingOrder.let { data ->
            when (data) {
                is UiState.Initial -> {}
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val order = data.data
                    if (order.wasteType?.isNotEmpty() == true && order.wasteQty != null && order.subtotalFee != null) {
                        OrderOngoingCard(
                            wasteType = order.wasteType,
                            wasteQty = order.wasteQty,
                            totalFee = order.subtotalFee,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.BottomCenter)
                                .clickable {
                                    navigateToDelivery(order.orderId ?: 0)
                                }
                        )
                    }
                }

                is UiState.Error -> {
                    Log.d("HomeScreen", "ongoingOrder: ${data.errorMsg}")
                }
            }
        }
    }

}

@Preview
@Composable
fun HomeCollectorContentPreview() {
    BersihKanTheme {
        HomeCollectorContent(
            name = "Hikaru",
            profile = R.drawable.ic_collector_profile,
            histories = UiState.Success(DataDummy.detailOrderResponse),
            ongoingOrder = UiState.Success(DataDummy.detailOrderResponse[3]),
            navigateToDetail = {},
            navigateToDelivery = {},
        )
    }
}