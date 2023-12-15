package com.example.bersihkan.ui.screen.customer.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.bersihkan.R
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponseItem
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
import com.example.bersihkan.utils.UserRole
import com.example.kekkomiapp.ui.common.UiState

@Composable
fun HomeScreen(
    navigateToHomeCustomer: () -> Unit,
    navigateToHomeCollector: ()-> Unit,
    navigateToWelcomePage1: () -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    viewModel.getSession()
    viewModel.userModel.collectAsState().value.let { userModel ->
        if(!userModel.isLogin){
            navigateToWelcomePage1()
        }
    }
    viewModel.fetchDataIfNeeded()

    val user = viewModel.userModel.collectAsState().value

    val histories = viewModel.histories.collectAsState().value
    val contents = viewModel.contents.collectAsState().value

    HomeContent(
        query = "",
        onQueryChange = {},
        onClickOrderCard = { /*TODO*/ },
        histories = histories,
        contents = contents,
        navigateToDetail = { /*TODO*/ },
        name = user.name,
        profile = if (user.role == UserRole.USER) R.drawable.ic_user_profile_2 else R.drawable.ic_collector_profile,
        navigateToStatistics = { /*TODO*/ })

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
    onQueryChange: (String) -> Unit,
    onClickOrderCard: () -> Unit,
    histories: UiState<List<DetailOrderResponseItem>>,
    contents: UiState<List<ContentsResponse>>,
    navigateToDetail: () -> Unit,
    name: String,
    profile: Int,
    navigateToStatistics: () -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier
            .padding(bottom = 20.dp)
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
                    onQueryChange = onQueryChange,
                    onClick = onClickOrderCard,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .offset(0.dp, 150.dp)
                )
            }
        }
        item {
            histories.let { data ->
                when(data){
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
                                            .size(50.dp)
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
                        val statistics = calculateStatisticsTotals(data.data, LocalContext.current)
                        HomeSection(
                            title = stringResource(R.string.statistics),
                            content = {
                                if (statistics.isNotEmpty()){
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
                }
            }
        }
        item {
            contents.let { data ->
                when(data){
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
                                            .size(50.dp)
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
                                if (contents.isNotEmpty()){
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
            when(data){
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
                                    .size(50.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
                is UiState.Success -> {
                    val hist = data.data
                    if(hist.isNotEmpty()){
                        items(hist, key = { it.orderId!! }){ history ->
                            RecentTransactionsCard(
                                date = convertToDate(history.orderDatetime ?: "").toString(),
                                address = history.pickupDatetime ?: "",
                                type = history.wasteType ?: "",
                                weight = history.wasteQty.toString(),
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                            )
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
            item {
                StatisticsCard(
                    count = statistics[0].qty.toString(),
                    title = statistics[0].item,
                    desc = statistics[0].desc,
                    backgroundColor = Java)
            }
            item {
                StatisticsCard(
                    count = statistics[1].qty.toString(),
                    title = statistics[1].item,
                    desc = statistics[1].desc,
                    backgroundColor = Cerulean)
            }
            item {
                StatisticsCard(
                    count = statistics[2].qty.toString(),
                    title = statistics[2].item,
                    desc = statistics[2].desc,
                    backgroundColor = PacificBlue,
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
            items(contents, key = { it.id!! }){ content ->
                FunFactsCard(
                    title = content.contentTitle ?: "No Title",
                    desc = content.contentText ?: "No description",
                    modifier = if(contents.indexOf(content) == contents.lastIndex) Modifier.padding(end = 20.dp) else Modifier
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
            onQueryChange = { newValue ->
                query = newValue
            },
            contents = contents,
            histories = histories,
            name = "Elizabeth",
            profile = R.drawable.ic_user_profile_2,
            navigateToDetail = { /*TODO*/ },
            navigateToStatistics = {},
            onClickOrderCard = {}
        )
    }
}