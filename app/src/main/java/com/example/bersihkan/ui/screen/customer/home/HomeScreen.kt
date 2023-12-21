package com.example.bersihkan.ui.screen.customer.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.bersihkan.BuildConfig
import com.example.bersihkan.R
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.ui.components.cards.FunFactsCard
import com.example.bersihkan.ui.components.section.HomeSection
import com.example.bersihkan.ui.components.cards.OrderNowCard
import com.example.bersihkan.ui.components.cards.RecentTransactionsCard
import com.example.bersihkan.ui.components.cards.StatisticsCard
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.gradient1
import com.example.bersihkan.ui.theme.headlineLarge
import com.example.bersihkan.ui.theme.headlineSmall
import com.example.bersihkan.ui.theme.textMediumMedium
import com.example.bersihkan.ui.theme.textRegularExtraLarge
import com.example.bersihkan.utils.Statistics
import com.example.bersihkan.helper.calculateStatisticsTotals
import com.example.bersihkan.helper.convertToDate
import com.example.bersihkan.notification.NotificationWorker
import com.example.bersihkan.ui.components.cards.OrderOngoingCard
import com.example.bersihkan.ui.components.modal.RegisterLoginDialog
import com.example.bersihkan.ui.theme.Botticelli
import com.example.bersihkan.ui.theme.Mercury
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.utils.Event
import com.example.bersihkan.utils.UserRole
import com.example.kekkomiapp.ui.common.UiState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

@Composable
fun HomeScreen(
    navigateToHomeCustomer: () -> Unit,
    navigateToHomeCollector: () -> Unit,
    navigateToSearch: (String) -> Unit,
    navigateToStatistics: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToDelivery: (Int) -> Unit,
    navigateToOrder: (Float, Float) -> Unit,
    navigateToWelcomePage1: () -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    workManager: WorkManager = WorkManager.getInstance(LocalContext.current),
    modifier: Modifier = Modifier
) {

    viewModel.getSession()
    LaunchedEffect(key1 = viewModel) {
        viewModel.refreshData()
    }

    viewModel.userModel.collectAsState().value.let { userModel ->
        if (!userModel.isLogin) {
            navigateToWelcomePage1()
        }
        if (userModel.role == UserRole.COLLECTOR) {
            navigateToHomeCollector()
        }
    }

    val context = LocalContext.current

    val fusedLocationClient: FusedLocationProviderClient = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val isPermissionGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val isPermissionNotificationGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    val requestPermissionLocationLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getLocation(fusedLocationClient, context) { lat, lon ->
                    viewModel.lat.floatValue = lat.toFloat()
                    viewModel.lon.floatValue = lon.toFloat()
                }
            } else {
                Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

    val requestPermissionNotificationLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

    if (isPermissionNotificationGranted) {

    } else {
        DisposableEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestPermissionNotificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            onDispose { }
        }
    }

    if (isPermissionGranted) {
        getLocation(fusedLocationClient, context) { lat, lon ->
            viewModel.lat.floatValue = lat.toFloat()
            viewModel.lon.floatValue = lon.toFloat()
        }
        viewModel.getLocationName()
    } else {
        DisposableEffect(Unit) {
            requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            onDispose { }
        }
    }

    viewModel.isSearching.collectAsState().value.let { isSearching ->
        if (isSearching) {
            Log.d("HomeScreen", "isSearching: Search Screen is showed")

//            SearchScreen(
//                query = viewModel.locationName.value,
//                onLocationClicked = { latLng ->
//                    viewModel.lat.floatValue = latLng.latitude.toFloat()
//                    viewModel.lon.floatValue = latLng.longitude.toFloat()
//                    viewModel.isNowSearching(false)
//                },
//                onErrorFetchData = {
//                    viewModel.isErrorLocationShowed.value = true
//                }
//            )
        } else {
            Log.d("HomeScreen", "isSearching: $isSearching")
        }
    }

    if (viewModel.isErrorLocationShowed.value) {
        RegisterLoginDialog(
            title = stringResource(R.string.error),
            message = stringResource(R.string.failed_to_load_location_data),
            onDismiss = {
                viewModel.isNowSearching(false)
                viewModel.isErrorLocationShowed.value = false
            }
        )
    }

    val user = viewModel.userModel.collectAsState().value

    val histories = viewModel.histories.collectAsState(initial = UiState.Loading).value
    val contents = viewModel.contents.collectAsState(initial = UiState.Loading).value
    val ongoingOrder = viewModel.ongoingOrder.collectAsState(initial = UiState.Loading).value
    val locationName = viewModel.locationName.collectAsState().value
    val isEnable = viewModel.isEnable.collectAsState().value

    viewModel.notification.collectAsState().value.let { notification ->
        notification.getContentIfNotHandled().let { isShowed ->
            if (isShowed == true) {
                val dataNotif = Data.Builder()
                    .putString(NotificationWorker.EXTRA_USER, UserRole.USER.role)
                    .putString(NotificationWorker.EXTRA_STATUS, viewModel.orderStatus.value.status)
                    .build()
                val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                    .setInputData(dataNotif)
                    .build()
                workManager.enqueue(oneTimeWorkRequest)
            }
        }
    }

    HomeContent(
        query = locationName,
        onClickOrderCard = {
            navigateToOrder(viewModel.lat.floatValue, viewModel.lon.floatValue)
        },
        histories = histories,
        contents = contents,
        isEnable = isEnable,
        navigateToSearch = { navigateToSearch(viewModel.locationName.value) },
        navigateToDetail = navigateToDetail,
        name = user.name,
        profile = if (user.role == UserRole.USER) R.drawable.ic_user_profile_2 else R.drawable.ic_collector_profile,
        navigateToStatistics = navigateToStatistics,
        ongoingOrder = ongoingOrder,
        navigateToDelivery = navigateToDelivery,
        modifier = modifier,
        onLocationClicked = { latLng ->
            viewModel.lat.floatValue = latLng.latitude.toFloat()
            viewModel.lon.floatValue = latLng.longitude.toFloat()
            viewModel.isNowSearching(false)
        },
        onErrorFetchData = {
            viewModel.isErrorLocationShowed.value = true
        }
    )

//    Box(
//        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center,
//    ) {
//        Text("This is a Home Screen")
//    }

}

@Composable
fun HomeContent(
    query: String,
    onClickOrderCard: () -> Unit,
    onLocationClicked: (LatLng) -> Unit,
    onErrorFetchData: (String) -> Unit,
    navigateToSearch: () -> Unit,
    histories: UiState<List<DetailOrderResponse>>,
    contents: UiState<List<ContentsResponse>>,
    ongoingOrder: UiState<DetailOrderResponse>,
    name: String,
    profile: Int,
    isEnable: Boolean,
    navigateToStatistics: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToDelivery: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Box {
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
                    OrderNowCard(
                        query = query,
                        onLocationClicked = onLocationClicked,
                        onErrorFetchData = onErrorFetchData,
                        onClick = onClickOrderCard,
                        isEnable = isEnable,
                        onSearchBarClick = navigateToSearch,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .offset(0.dp, 150.dp)
                    )
                }
            }
            item {
                histories.let { data ->
                    when (data) {
                        is UiState.Initial -> {

                        }

                        is UiState.Loading -> {
                            HomeSection(
                                title = stringResource(R.string.statistics),
                                content = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(175.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            color = Grey,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                },
                                isStatistic = true,
                                navigateToStatistic = navigateToStatistics,
                                modifier = Modifier.padding(top = 90.dp, start = 20.dp)
                            )
                        }

                        is UiState.Success -> {
                            val statistics =
                                calculateStatisticsTotals(data.data, LocalContext.current)
                            HomeSection(
                                title = stringResource(R.string.statistics),
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
                                isStatistic = true,
                                navigateToStatistic = navigateToStatistics,
                                modifier = Modifier.padding(top = 90.dp, start = 20.dp)
                            )
                        }

                        is UiState.Error -> {
                            HomeSection(
                                title = stringResource(R.string.statistics),
                                content = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(175.dp)
                                    ) {
                                        Text(
                                            text = data.errorMsg,
                                            style = textMediumMedium,
                                            color = Grey
                                        )
                                    }
                                },
                                isStatistic = true,
                                navigateToStatistic = navigateToStatistics,
                                modifier = Modifier.padding(top = 90.dp, start = 20.dp)
                            )
                        }

                        else -> {}
                    }
                }
            }
            item {
                contents.let { data ->
                    when (data) {
                        is UiState.Initial -> {}
                        is UiState.Loading -> {
                            HomeSection(
                                title = stringResource(R.string.fun_facts),
                                content = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(128.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            color = Grey,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                },
                                navigateToStatistic = {},
                                modifier = Modifier
                                    .padding(top = 30.dp, start = 20.dp)
                            )
                        }

                        is UiState.Success -> {
                            val contents = data.data
                            HomeSection(
                                title = stringResource(R.string.fun_facts),
                                content = {
                                    if (contents.isNotEmpty()) {
                                        FunFactsCardRow(contents = contents)
                                    } else {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(128.dp)
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.no_data_found),
                                                style = textMediumMedium,
                                                color = Grey
                                            )
                                        }
                                    }
                                },
                                navigateToStatistic = {},
                                modifier = Modifier
                                    .padding(top = 30.dp, start = 20.dp)
                            )
                        }

                        is UiState.Error -> {
                            HomeSection(
                                title = stringResource(R.string.fun_facts),
                                content = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(128.dp)
                                    ) {
                                        Text(
                                            text = data.errorMsg,
                                            style = textMediumMedium,
                                            color = Grey
                                        )
                                    }
                                },
                                navigateToStatistic = {},
                                modifier = Modifier
                                    .padding(top = 30.dp, start = 20.dp)
                            )
                        }

                        else -> {}
                    }
                }
            }
            item {
                Text(
                    text = stringResource(R.string.recent_transactions),
                    style = headlineSmall,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 30.dp)
                )
            }
            histories.let { data ->
                when (data) {
                    is UiState.Initial -> {}
                    is UiState.Loading -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = Grey,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }

                    is UiState.Success -> {
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

                    is UiState.Error -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            ) {
                                Text(
                                    text = data.errorMsg,
                                    style = textMediumMedium,
                                    color = Grey
                                )
                            }
                        }
                    }

                    else -> {}
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

                else -> {}
            }
        }
    }

}

@Composable
fun Banner(
    name: String,
    profile: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(270.dp)
            .background(gradient1(1500f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Hello,",
                    style = textRegularExtraLarge,
                    color = Color.White
                )
                Text(
                    text = name,
                    style = headlineLarge,
                    color = Color.White
                )
            }
            Image(
                painter = painterResource(id = profile),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .offset(0.dp, 5.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 64.dp,
                        topEnd = 64.dp,
                    )
                )
                .background(Color.White)
        )

    }
}

@Composable
fun StatisticCardRow(
    statistics: List<Statistics>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
        state = rememberLazyListState(),
        content = {
            items(statistics, key = { it.desc }) { statistic ->
                StatisticsCard(
                    count = statistic.qty.toString(),
                    title = statistic.item,
                    desc = statistic.desc,
                    backgroundColor = when (statistics.indexOf(statistic)) {
                        0 -> Java
                        1 -> Cerulean
                        2 -> PacificBlue
                        // Add more colors or logic as needed for different indices
                        else -> Color.Gray // Fallback color for additional items
                    },
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        })
}

@Composable
fun FunFactsCardRow(
    contents: List<ContentsResponse>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
        state = rememberLazyListState(),
        content = {
            items(contents, key = { it.id!! }) { content ->
                FunFactsCard(
                    title = content.contentTitle ?: "No Title",
                    desc = content.contentText ?: "No description",
                    modifier = if (contents.indexOf(content) == contents.lastIndex) Modifier.padding(
                        end = 20.dp
                    ) else Modifier
                )
            }
        })
}

@Preview
@Composable
fun BannerPreview() {
    BersihKanTheme {
        Banner("Elizabeth", R.drawable.ic_user_profile_2)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeContentPreview() {
    BersihKanTheme {
        var query by remember {
            mutableStateOf("")
        }
        val contents = UiState.Success(DataDummy.contentResponseList)
        val histories = UiState.Success(DataDummy.detailOrderResponse)
        HomeContent(
            query = query,
            onLocationClicked = {},
            onErrorFetchData = {},
            contents = contents,
            histories = histories,
            name = "Elizabeth",
            profile = R.drawable.ic_user_profile_2,
            navigateToDetail = { /*TODO*/ },
            navigateToStatistics = {},
            onClickOrderCard = {},
            ongoingOrder = UiState.Success(DataDummy.detailOrderResponse[1]),
            navigateToDelivery = {},
            isEnable = true,
            navigateToSearch = {}
        )
    }
}

fun getLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    onLocationResult: (Double, Double) -> Unit
) {

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: android.location.Location? ->
                Log.d("HomeScreen", "getLocation: $location")
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("HomeScreen", "getLocation: lat=$latitude, lon=$longitude")
                    onLocationResult(latitude, longitude)
                } else {
//                    Toast.makeText(
//                        context,
//                        "Location is not found. Try again",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }
    } else {
        Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show()
    }

}