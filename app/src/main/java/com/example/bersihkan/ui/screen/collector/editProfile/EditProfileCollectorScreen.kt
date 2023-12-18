package com.example.bersihkan.ui.screen.collector.editProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.ui.components.buttons.MediumButton
import com.example.bersihkan.ui.components.textFields.NameTextField
import com.example.bersihkan.ui.components.textFields.PhoneNumberTextField
import com.example.bersihkan.ui.components.textFields.TextFieldContent
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.gradient1
import com.example.bersihkan.ui.theme.headlineSmall
import com.example.bersihkan.ui.theme.textMediumMedium
import com.example.bersihkan.ui.theme.textMediumSmall
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.ui.components.modal.ConfirmDialog
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.kekkomiapp.ui.common.UiState

@Composable
fun EditProfileCollectorScreen(
    saveOnClick: () -> Unit,
    viewModel: EditProfileCollectorViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(key1 = viewModel, block = {
        viewModel.getSession()
        viewModel.getUserData()
    })

    viewModel.user.collectAsState().value.let{ userData ->
        when(userData){
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
            is UiState.Success ->{
                val user = userData.data
                viewModel.apply {
//                    inputName.value = user.name.toString()
//                    inputPhone.value = user.phone.toString()
                    EditProfileCollectorContent(
                        username = user.username.toString(),
                        email = user.email.toString(),
                        facilityName = user.facilityName.toString(),
                        inputName = inputName.value,
                        onNameChange = { newValue ->
                            inputName.value = newValue
                            enableButton()
                        },
                        inputPhoneNumber = inputPhone.value,
                        onPhoneNumberChange = { newValue ->
                            inputPhone.value = newValue
                            enableButton()
                        },
                        isEnabled = isEnabled.value,
                        saveOnClick = {
                            viewModel.saveProfile()
                            saveOnClick()
                        },
                        navigateToBack = navigateToBack
                    )
                }
            }
            is UiState.Error -> {
                EditProfileCollectorContent(
                    username = "",
                    email = "",
                    facilityName = "",
                    inputName = "",
                    onNameChange = {},
                    inputPhoneNumber = "",
                    onPhoneNumberChange = {},
                    isEnabled = false,
                    saveOnClick = {},
                    isError = true,
                    textError = userData.errorMsg,
                    navigateToBack = navigateToBack
                )
            }
        }
    }

}

@Composable
fun EditProfileCollectorContent(
    username: String,
    email:String,
    facilityName: String,
    inputName: String,
    onNameChange: (String) -> Unit,
    inputPhoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isError: Boolean = false,
    textError: String = "",
    isEnabled: Boolean,
    saveOnClick: () -> Unit,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isDialogShowed by remember {
        mutableStateOf(true)
    }

    if(!isDialogShowed){
        ConfirmDialog(
            title = stringResource(id = R.string.edit_profile),
            message = stringResource(R.string.edit_profile_confirm),
            onPositive = saveOnClick,
            onDismiss = { isDialogShowed = true }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient1(800f))
    ) {
        Surface(
            shape = Shapes.extraLarge.copy(topStart = CornerSize(60.dp), topEnd = CornerSize(60.dp), bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp)),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(620.dp)
                .align(Alignment.BottomCenter)
        ) {
        }
        LazyColumn(
            state = rememberLazyListState()
        ){
            item {
                TopBar(
                    text = stringResource(id = R.string.edit_profile),
                    onBackClick = navigateToBack,
                    color = Color.White,
                    enableOnBack = true
                )
            }
            if(isError){
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Text(
                            text = textError,
                            style = textMediumMedium,
                            color = Grey,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_user_profile_2),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.BottomCenter)
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = username,
                                style = headlineSmall.copy(
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Text(
                                text = stringResource(
                                    R.string.collector_profile,
                                    email,
                                    facilityName
                                ),
                                style = textMediumSmall.copy(
                                    color = Grey,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 30.dp)
                        ){
                            TextFieldContent(
                                title = stringResource(R.string.name),
                                content = {
                                    NameTextField(
                                        input = inputName,
                                        onInputChange = onNameChange
                                    )
                                }
                            )
                            TextFieldContent(
                                title = stringResource(R.string.phone_number),
                                content = {
                                    PhoneNumberTextField(
                                        input = inputPhoneNumber,
                                        onInputChange = onPhoneNumberChange
                                    )
                                }
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth()
                        ) {
                            MediumButton(
                                text = stringResource(id = R.string.save),
                                isEnabled = isEnabled,
                                color = BlueLagoon,
                                onClick = { isDialogShowed = false },
                                modifier = Modifier
                                    .padding(6.dp, 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(
    showSystemUi = true
)
@Composable
fun EditContentPreview() {
    BersihKanTheme {
        val user = DataDummy.detailCollectorResponse1
        EditProfileCollectorContent(
            username = user.username.toString(),
            email = user.email.toString(),
            inputName = user.collectorName.toString(),
            onNameChange = {},
            inputPhoneNumber = user.phone.toString(),
            onPhoneNumberChange = {},
            saveOnClick = { /*TODO*/ },
            isEnabled = true,
            navigateToBack = {},
            facilityName = user.facilityName.toString()
        )
    }
}