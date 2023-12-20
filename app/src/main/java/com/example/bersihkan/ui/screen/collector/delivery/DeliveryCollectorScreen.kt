package com.example.bersihkan.ui.screen.collector.delivery

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.bersihkan.R
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.helper.convertToDate
import com.example.bersihkan.ui.components.cards.OrderSummaryCard
import com.example.bersihkan.ui.components.section.HomeSection
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.gradient2
import com.example.bersihkan.utils.OrderStatus
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.bersihkan.BuildConfig
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.data.remote.response.DetailOrderCollectorAll
import com.example.bersihkan.helper.findOrderStatus
import com.example.bersihkan.ui.components.buttons.LargeButton
import com.example.bersihkan.ui.components.buttons.MediumButton
import com.example.bersihkan.ui.components.buttons.SmallButton
import com.example.bersihkan.ui.components.cards.DeliveryCollectorCard
import com.example.bersihkan.ui.components.modal.RegisterLoginDialog
import com.example.bersihkan.ui.components.textFields.NotesInput
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.headlineExtraSmall
import com.example.bersihkan.ui.theme.textMediumMedium
import com.example.bersihkan.ui.theme.textMediumSmall
import com.example.bersihkan.utils.UserRole
import com.example.kekkomiapp.ui.common.UiState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DeliveryCollectorScreen(
    orderId: Int,
    navigateToBack: () -> Unit,
    viewModel: DeliveryCollectorViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    LaunchedEffect(
        key1 = viewModel,
        block = {
            viewModel.getDetailHistoryById(id = orderId)
        })

    viewModel.orderData.collectAsState().value.let { orderData ->
        when(orderData){
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
                DeliveryCollectorContent(
                    orderData = orderData.data,
                    orderStatus = viewModel.orderStatus.value,
                    navigateToBack = navigateToBack,
                    onClickButton = { viewModel.updateOrderStatus(orderId) },
                    isEnabled = viewModel.isEnabled,
                    selectedPict = viewModel.selectedPict.value.toUri(),
                    onPictSelected = { uri ->
                        viewModel.selectedPict.value = uri.toString()
                        viewModel.refreshData()
                    }
                )
            }
            is UiState.Error -> {
                DeliveryCollectorContent(
                    orderData = DetailOrderCollectorAll(),
                    orderStatus = viewModel.orderStatus.value,
                    navigateToBack = navigateToBack,
                    isError = true,
                    textError = orderData.errorMsg,
                    onClickButton = {},
                    isEnabled = false,
                    selectedPict = viewModel.selectedPict.value.toUri(),
                    onPictSelected = { uri ->
                        viewModel.selectedPict.value = uri.toString()
                        viewModel.refreshData()
                    }
                )
            }
        }
    }

    var showDialog by remember {
        mutableStateOf(true)
    }

    viewModel.response.collectAsState().value.let { response ->
        when(response){
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
                if(showDialog){
                    RegisterLoginDialog(
                        title = stringResource(R.string.update_status),
                        message = stringResource(R.string.status_updates),
                        onDismiss = {
                            showDialog = false
                            navigateToBack()
                        }
                    )
                }
            }
            is UiState.Error -> {
                if(showDialog){
                    RegisterLoginDialog(
                        title = stringResource(R.string.error),
                        message = response.errorMsg,
                        onDismiss = { showDialog = false }
                    )
                }
            }
            else -> {}
        }
    }

}

@Composable
fun DeliveryCollectorContent(
    orderData: DetailOrderCollectorAll,
    orderStatus: OrderStatus,
    isError: Boolean = false,
    textError: String = "",
    isEnabled: Boolean,
    selectedPict: Uri,
    onPictSelected: (Uri) -> Unit,
    onClickButton: () -> Unit,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext

    var showModal by remember {
        mutableStateOf(false)
    }

    var currentImageUri by remember {
        mutableStateOf(getImageUri(context))
    }

    // Function to request the CAMERA permission

    val takePicture =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isTaken ->
            if (isTaken) {
                // Image was taken successfully
                // Do something with the captured image
                onPictSelected(currentImageUri)
            }
        }

    val pickFromGallery =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri?.scheme != "content" ) {
                Toast.makeText(context, "Invalid URI", Toast.LENGTH_SHORT).show()

            } else if(context.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context, "Insufficient permissions", Toast.LENGTH_SHORT).show()
            }
            else {
                onPictSelected(uri)
                // Handle insufficient permissions or invalid URI
            }
        }

    val requestPermissionLauncherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            val currentImageUri = getImageUri(context)
            takePicture.launch(currentImageUri)
        } else {
            Toast.makeText(context, "Camera permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    val requestPermissionLauncherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickFromGallery.launch("image/*")
        } else {
            Toast.makeText(context, "Camera permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    val detailOrder = orderData.detailOrderResponse
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
                    pickup = orderData.addressFromLatLng.toString(),
                    wasteType = detailOrder?.wasteType.toString(),
                    wasteQty = detailOrder?.wasteQty ?: 0,
                    totalFee = detailOrder?.subtotalFee ?: 0,
                    userName = detailOrder?.userName.toString(),
                    userPhone = detailOrder?.userPhone.toString(),
                    notes = detailOrder?.userNotes.toString(),
                    date = convertToDate(detailOrder?.orderDatetime.toString()),
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
                    title = stringResource(R.string.attach_proof),
                    content = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 20.dp)
                        ) {
                            SmallButton(
                                text = stringResource(R.string.camera),
                                color = Cerulean,
                                onClick = {
                                    requestPermissionLauncherCamera.launch(android.Manifest.permission.CAMERA)
                                },
                                isEnabled = true,
                                modifier = Modifier.weight(1f)
                            )
                            SmallButton(
                                text = stringResource(R.string.gallery),
                                color = Java,
                                onClick = {
                                    requestPermissionLauncherGallery.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                                },
                                isEnabled = true,
                                modifier = Modifier.weight(1f)
                            )
                            SmallButton(
                                text = stringResource(R.string.proof),
                                color = Cerulean,
                                onClick = { showModal = true },
                                isEnabled = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    },
                    color = Color.White,
                    navigateToStatistic = { },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item {
                LargeButton(
                    text = orderStatus.button,
                    color = BlueLagoon,
                    onClick = onClickButton,
                    isEnabled = isEnabled,
                    modifier = Modifier
                        .padding(20.dp)
                )
            }
        }
    }

    if(showModal){
        PictProofModal(
            showModal = showModal,
            image = selectedPict.toString(),
            onDismiss = { showModal = false }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictProofModal(
    showModal: Boolean,
    image: String,
    onDismiss: () -> Unit,
) {
    val imageUri = image.toUri()
    val modalState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        content = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if(image != ""){
                    val painter = // Apply transformations if needed (e.g., circular crop)
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = imageUri)
                                .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.(){}).build()
                        )
                    Image(
                        painter = painter,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(500.dp)
                            .padding(15.dp)
                            .clip(Shapes.large)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.not_uploaded_proof),
                        style = textMediumSmall.copy(
                            color = Grey
                        ),
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                            .height(70.dp)
                    )
                }
            }
        },
        containerColor = Color.White,
        onDismissRequest = onDismiss
    )
}

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/Pictures/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}

@Preview
@Composable
fun DeliveryCollectorContentPreview() {
    BersihKanTheme {
        DeliveryCollectorContent(
            orderData = DataDummy.detailOrderCollectorAll,
            orderStatus = findOrderStatus(DataDummy.detailOrderCollectorAll.detailOrderResponse?.orderStatus.toString()),
            navigateToBack = { /*TODO*/ },
            onClickButton = {},
            isEnabled = true,
            selectedPict = getImageUri(LocalContext.current),
            onPictSelected = {uri -> }
        )
    }
}
