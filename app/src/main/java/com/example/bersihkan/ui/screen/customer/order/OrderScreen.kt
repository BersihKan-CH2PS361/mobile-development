package com.example.bersihkan.ui.screen.customer.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bersihkan.R
import com.example.bersihkan.helper.findWasteType
import com.example.bersihkan.ui.components.buttons.LargeButton
import com.example.bersihkan.ui.components.buttons.SmallButton
import com.example.bersihkan.ui.components.cards.CartDetailsCard
import com.example.bersihkan.ui.components.cards.OrderSummaryCard
import com.example.bersihkan.ui.components.section.HomeSection
import com.example.bersihkan.ui.components.textFields.NotesInput
import com.example.bersihkan.ui.components.textFields.QuantityInput
import com.example.bersihkan.ui.components.textFields.SelectTypeDropdown
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.gradient2
import com.example.bersihkan.ui.theme.headlineExtraSmall
import com.example.bersihkan.ui.theme.subHeadlineSmall
import com.example.bersihkan.utils.WasteType

@Composable
fun OrderScreen(
    navigateToDelivery: () -> Unit,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {



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
        )
    }

    if(showNotesModal){
        NotesModal(
            showModal = showNotesModal,
            notes = notes,
            onCLick = { newValue ->
                notesOnCLick(newValue)
                showNotesModal = false
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .background(gradient2(800f, 1600f))
    ) {
        TopBar(
            text = stringResource(R.string.order),
            onBackClick = navigateToBack,
            color = Color.White,
            enableOnBack = true,
        )
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
                .weight(1f)
                .padding(20.dp)
        )
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
                .weight(1f)
                .padding(horizontal = 20.dp)
        )
        LargeButton(
            text = stringResource(id = R.string.order),
            color = BlueLagoon,
            onClick = orderOnClick,
            isEnabled = isEnabled,
            modifier = Modifier.padding(20.dp)
        )
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
        onDismissRequest ={} ,
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
        onDismissRequest = { }
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
        )
    }
}