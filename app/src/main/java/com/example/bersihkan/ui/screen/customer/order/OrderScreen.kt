package com.example.bersihkan.ui.screen.customer.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bersihkan.R
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.helper.findWasteType
import com.example.bersihkan.ui.components.buttons.LargeButton
import com.example.bersihkan.ui.components.buttons.SmallButton
import com.example.bersihkan.ui.components.cards.CartDetailsCard
import com.example.bersihkan.ui.components.cards.OrderSummaryCard
import com.example.bersihkan.ui.components.modal.RegisterLoginDialog
import com.example.bersihkan.ui.components.section.HomeSection
import com.example.bersihkan.ui.components.textFields.NotesInput
import com.example.bersihkan.ui.components.textFields.QuantityInput
import com.example.bersihkan.ui.components.textFields.SelectTypeDropdown
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.gradient2
import com.example.bersihkan.ui.theme.headlineExtraSmall
import com.example.bersihkan.ui.theme.subHeadlineSmall
import com.example.bersihkan.utils.WasteType
import com.example.kekkomiapp.ui.common.UiState

@Composable
fun OrderScreen(
    lat: Float,
    lon: Float,
    navigateToHome: () -> Unit,
    navigateToDelivery: (Int) -> Unit,
    navigateToBack: () -> Unit,
    viewModel: OrderViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    viewModel.lat.floatValue = lat
    viewModel.lon.floatValue = lon

    val locationName = viewModel.locationName.collectAsState().value
    val ongoingOrder = viewModel.ongoingOrder.collectAsState().value

    LaunchedEffect(key1 = viewModel, block = {
        viewModel.getLocationName()
    })

    Log.d("OrderViewModal", "subtotalFee: ${viewModel.subtotalFee}")

    OrderContent(
        locationName = locationName,
        wasteType = viewModel.wasteType.value,
        wasteQty = viewModel.wasteQty.intValue.toString(),
        notes = viewModel.notes.value,
        wasteFee = viewModel.wasteFee,
        isEnabled = viewModel.isEnabled,
        wasteDetailsOnClick = { waste, qty ->
            viewModel.wasteType.value = waste
            viewModel.wasteQty.intValue = qty.toInt()
            viewModel.refreshCount()
        },
        notesOnCLick = { newValue ->
            viewModel.notes.value = newValue
            viewModel.refreshCount()
        },
        orderOnClick = {
            viewModel.createOrder()
            viewModel.getCurrentOrderUser()
        },
        navigateToBack = navigateToBack
    )

    viewModel.response.collectAsState().value.let { response ->
        var showDialog by remember {
            mutableStateOf(true)
        }
        when(response){
            is UiState.Success -> {
               ongoingOrder.let {
                   when(it){
                       is UiState.Success -> {
                           val data = it.data
                           navigateToDelivery(data.orderId ?: -1)
                       }
                       else -> {}
                   }
               } }
            is UiState.Error -> {
                showDialog = true
                if(showDialog){
                    RegisterLoginDialog(
                        title = stringResource(R.string.order_failed),
                        message = response.errorMsg,
                        onDismiss = {
                            navigateToBack()
                            showDialog = false
                        }
                    )
                }
            }
            else -> {}
        }
    }

}

@Composable
fun OrderContent(
    locationName: String,
    wasteType: WasteType,
    wasteQty: String,
    notes: String,
    wasteFee: Int,
    isEnabled: Boolean,
    wasteDetailsOnClick: (WasteType, String) -> Unit,
    notesOnCLick: (String) -> Unit,
    orderOnClick: () -> Unit,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    var showWasteModal by remember { mutableStateOf(false) }
    var showNotesModal by remember { mutableStateOf(false) }

    if(showWasteModal){
        WasteDetailsModal(
            showModal = showWasteModal,
            onClickButton = { wasteType, wasteQty ->
                wasteDetailsOnClick(wasteType, wasteQty)
                showWasteModal = false
            },
            wasteType = wasteType,
            wasteQty = wasteQty,
            onDismiss = { showWasteModal = false }
        )
    }

    if(showNotesModal){
        NotesModal(
            showModal = showNotesModal,
            notes = notes,
            onCLick = { newValue ->
                notesOnCLick(newValue)
                showNotesModal = false
            },
            onDismiss = { showNotesModal = false }
        )
    }

    LazyColumn(
        state = LazyListState(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .background(gradient2(800f, 1600f))
    ) {
        item {
            TopBar(
                text = stringResource(R.string.order),
                onBackClick = navigateToBack,
                color = Color.White,
                enableOnBack = true,
            )
        }
        item {
            CartDetailsCard(
                pickup = locationName,
                wasteType = wasteType.type,
                wasteQty = wasteQty.toInt(),
                notes = notes,
                wasteDetailsOnClick = {
                    showWasteModal = true
                },
                notesOnCLick = {
                    showNotesModal = true
                },
                modifier = Modifier
                    .padding(20.dp)
            )
        }
        item {
            HomeSection(
                title = stringResource(R.string.order_summary),
                content = {
                    OrderSummaryCard(
                        wasteFee = wasteFee,
                        wasteType = wasteType.type,
                        wasteQty = wasteQty.toInt()
                    )
                },
                navigateToStatistic = { },
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.fillMaxHeight())
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                LargeButton(
                    text = stringResource(id = R.string.order),
                    color = BlueLagoon,
                    onClick = orderOnClick,
                    isEnabled = isEnabled,
                    modifier = Modifier
                        .padding(20.dp)
                )
            }
        }
    }

}

@Preview
@Composable
fun OrderContentPreview() {

    var wasteType by remember {
        mutableStateOf(WasteType.INITIAL)
    }
    var wasteQty by remember {
        mutableStateOf("0")
    }
    var notes by  remember {
        mutableStateOf("")
    }

    BersihKanTheme {
        OrderContent(
            locationName = "Location 1",
            wasteType = wasteType,
            wasteQty = wasteQty,
            notes = notes,
            wasteFee = wasteQty.toInt().times(wasteType.price),
            orderOnClick = { },
            navigateToBack = {},
            isEnabled = true,
            wasteDetailsOnClick = {type, s ->
                wasteType = type
                wasteQty = s
            },
            notesOnCLick = { newVal ->
                notes = newVal
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteDetailsModal(
    showModal: Boolean,
    onClickButton: (WasteType, String) -> Unit,
    wasteType: WasteType,
    wasteQty: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier.background(Color.Transparent)
) {
    var initWasteType by remember {
        mutableStateOf(WasteType.INITIAL)
    }
    var initWasteQty by remember {
        mutableStateOf("")
    }
    initWasteType = wasteType
    initWasteQty = wasteQty
    val modalState = rememberModalBottomSheetState()
    LaunchedEffect(key1 = showModal, block = {
        if(showModal) modalState.show() else modalState.hide()
    })
    ModalBottomSheet(
        onDismissRequest = onDismiss ,
        sheetState = modalState,
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.waste_details),
                    style = headlineExtraSmall.copy(
                        color = Color.Black
                    ),
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SelectTypeDropdown(
                        selectedItem = initWasteType,
                        onItemSelected = { newValue ->
                            initWasteType = newValue
                        }
                    )
                    QuantityInput(
                        input = initWasteQty,
                        onInputChange = { newValue ->
                            initWasteQty = newValue
                        }
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    SmallButton(
                        text = stringResource(id = R.string.ok_text),
                        color = Cerulean,
                        onClick = { onClickButton(initWasteType, initWasteQty) },
                        isEnabled = initWasteQty != "" && initWasteType != WasteType.INITIAL,
                    )
                }
            }
        },
        containerColor = Color.White,
        shape = Shapes.medium.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotesModal(
    showModal: Boolean,
    notes: String,
    onDismiss: () -> Unit,
    onCLick: (String) -> Unit
) {
    var initNotes by remember {
        mutableStateOf("")
    }
    initNotes = notes
    val modalState = rememberModalBottomSheetState()
    LaunchedEffect(key1 = showModal, block = {
        if(showModal) modalState.show() else modalState.hide()
    })
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.notes),
                    style = headlineExtraSmall.copy(
                        color = Color.Black
                    ),
                )
                NotesInput(
                    input = initNotes,
                    onInputChange = { newValue ->
                        initNotes = newValue
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    SmallButton(
                        text = stringResource(id = R.string.ok_text),
                        color = Cerulean,
                        onClick = { onCLick(initNotes) },
                        isEnabled = initNotes != "" ,
                    )
                }
            }
        },
        containerColor = Color.White,
        onDismissRequest = onDismiss
    )
}

@Preview(showSystemUi = true)
@Composable
fun WasteModalPreview() {
    var wasteType by remember {
        mutableStateOf(WasteType.INITIAL)
    }
    var wasteQty by remember {
        mutableStateOf("")
    }
    var showModal by remember {
        mutableStateOf(false)
    }
    BersihKanTheme {
        WasteDetailsModal (
            wasteQty = wasteQty,
            wasteType = wasteType,
            onClickButton = { type, qty ->
                wasteType = type
                wasteQty = qty
                showModal = true
            },
            showModal = showModal,
            onDismiss = {}
        )
    }
}