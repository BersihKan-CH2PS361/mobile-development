package com.example.bersihkan.ui.screen.general.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.components.ClickableText
import com.example.bersihkan.ui.components.textFields.EmailTextField
import com.example.bersihkan.ui.components.section.LoginRegisterSection
import com.example.bersihkan.ui.components.textFields.NameTextField
import com.example.bersihkan.ui.components.textFields.PasswordTextField
import com.example.bersihkan.ui.components.textFields.PhoneNumberTextField
import com.example.bersihkan.ui.components.textFields.TextFieldContent
import com.example.bersihkan.ui.components.textFields.UsernameTextField
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.helper.isEmailValid
import com.example.bersihkan.helper.isPasswordValid
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.ui.components.buttons.MediumButton
import com.example.bersihkan.ui.components.modal.RegisterLoginDialog
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.bersihkan.ui.theme.Grey
import com.example.kekkomiapp.ui.common.UiState

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit,
    navigateToWelcomeScreen2: () -> Unit,
    viewModel: RegisterViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    val inputName = viewModel.inputName.value
    val inputUsername = viewModel.inputUsername.value
    val inputPhoneNumber = viewModel.inputPhoneNumber.value
    val inputEmail = viewModel.inputEmail.value
    val errorEmail = viewModel.errorEmail.value
    val inputPassword = viewModel.inputPassword.value
    val errorPassword = viewModel.errorPassword.value
    val inputConfirmPassword = viewModel.inputConfirmPassword.value
    val errorConfirmPassword = viewModel.errorConfirmPassword.value
    val isEnabled = viewModel.isEnabled.collectAsState().value

    RegisterContent(
        inputName = inputName,
        onNameChange = { viewModel.setName(it) },
        inputUsername = inputUsername,
        onUsernameChange = { viewModel.setUsername(it) },
        inputEmail = inputEmail,
        errorEmail = errorEmail,
        onEmailChange = {
            viewModel.setEmail(it)
            viewModel.setErrorEmail(!isEmailValid(it))
        },
        inputPhoneNumber = inputPhoneNumber,
        onPhoneNumberChange = { viewModel.setPhoneNumber(it) },
        inputPassword = inputPassword,
        errorPassword = errorPassword,
        onPasswordChange = {
            viewModel.setPassword(it)
            viewModel.setErrorPassword(!isPasswordValid(it))
        },
        inputConfirmPassword = inputConfirmPassword,
        errorConfirmPassword = errorConfirmPassword,
        onConfirmPasswordChange = {
            viewModel.setConfirmPassword(it)
            viewModel.setErrorConfirmPassword(!isPasswordValid(it))
        },
        navigateToLogin = navigateToLogin,
        registerOnClick = {
            viewModel.register()
        },
        isEnabled = isEnabled
    )

    viewModel.response.collectAsState().value.let { response ->
        var showDialog by remember {
            mutableStateOf(true)
        }
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
                RegisterLoginDialog(
                    title = stringResource(R.string.hello),
                    message = stringResource(R.string.register_success),
                    onDismiss = {
                        showDialog = false
                        navigateToLogin()
                    }
                )
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
        }
    }

}

@Composable
fun RegisterContent(
    inputName: String,
    onNameChange: (String) -> Unit,
    inputUsername: String,
    onUsernameChange: (String) -> Unit,
    inputEmail: String,
    errorEmail: Boolean,
    onEmailChange: (String) -> Unit,
    inputPhoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    inputPassword: String,
    errorPassword: Boolean,
    onPasswordChange: (String) -> Unit,
    inputConfirmPassword: String,
    errorConfirmPassword: Boolean,
    onConfirmPasswordChange: (String) -> Unit,
    navigateToLogin: () -> Unit,
    registerOnClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
){

    LazyColumn(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        item {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(200.dp)
                    .padding(bottom = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "BersihKan App Logo",
                    modifier = Modifier
                        .size(130.dp)
                )
            }
        }
        item {
            LoginRegisterSection(
                title = stringResource(id = R.string.register),
                description = stringResource(id = R.string.register_desc)
            )
        }
        item {
            Divider(
                thickness = 2.dp,
                color = BlueLagoon,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 30.dp)
                    .width(170.dp)
            )
        }
        item {
            TextFieldContent(
                title = stringResource(R.string.username),
                content = {
                    UsernameTextField(
                        input = inputUsername,
                        onInputChange = onUsernameChange
                    )
                }
            )
        }
        item {
            TextFieldContent(
                title = stringResource(R.string.name),
                content = {
                    NameTextField(
                        input = inputName,
                        onInputChange = onNameChange
                    )
                }
            )
        }
        item {
            TextFieldContent(
                title = stringResource(id = R.string.email),
                content = {
                    EmailTextField(
                        input = inputEmail,
                        onInputChange = onEmailChange,
                        isError = errorEmail
                    )
                }
            )
        }
        item {
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
        item {
            TextFieldContent(
                title = stringResource(R.string.password),
                content = {
                    PasswordTextField(
                        input = inputPassword,
                        onInputChange = onPasswordChange,
                        isError = errorPassword,
                        placeholderText = stringResource(id = R.string.enter_password)
                    )
                }
            )
        }
        item {
            TextFieldContent(
                title = stringResource(R.string.confirm_password),
                content = {
                    PasswordTextField(
                        input = inputConfirmPassword,
                        onInputChange = onConfirmPasswordChange,
                        isError = errorConfirmPassword,
                        placeholderText = stringResource(id = R.string.enter_confirm_password)
                    )
                }
            )
        }
        item {
            ClickableText(
                text = stringResource(id = R.string.already_have_an_account),
                clickableText = stringResource(id = R.string.login),
                onClick = navigateToLogin,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }
        item {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                MediumButton(
                    text = stringResource(id = R.string.register),
                    isEnabled = isEnabled,
                    color = BlueLagoon,
                    onClick = registerOnClick,
                    modifier = Modifier
                        .padding(6.dp, 2.dp)
                )
            }
        }

    }

}

@Composable
fun RegisterButton(
    message: String,
    navigateToHome: () -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    if(showDialog){
        RegisterLoginDialog (
            title = stringResource(id = R.string.welcome_back),
            message = message,
            onDismiss = {
                showDialog = false
                navigateToHome()
            }
        )
    }

    Button(
        onClick = {
            if(!showDialog){
                showDialog = true
            }
        },
        modifier = Modifier
            .padding(6.dp, 2.dp)
    ) {
        Text(text = stringResource(id = R.string.register))
    }

}

@Preview
@Composable
fun RegisterDialogPreview() {
    BersihKanTheme {
        RegisterLoginDialog(
            title = stringResource(id = R.string.welcome_back),
            message = "Register berhasil!"
        ) {

        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterContentPreview() {

    var inputName by remember {
        mutableStateOf("")
    }
    var inputUsername by remember {
        mutableStateOf("")
    }
    var inputEmail by remember {
        mutableStateOf("")
    }
    var errorEmail by remember {
        mutableStateOf(false)
    }
    var inputPassword by remember {
        mutableStateOf("")
    }
    var errorPassword by remember {
        mutableStateOf(false)
    }
    var inputConfirmPassword by remember {
        mutableStateOf("")
    }
    var errorConfirmPassword by remember {
        mutableStateOf(false)
    }
    var inputPhoneNumber by remember {
        mutableStateOf("")
    }

    BersihKanTheme {
        RegisterContent(
            inputUsername = inputUsername,
            onUsernameChange = { newValue ->
                inputName = newValue
            },
            inputName = inputName,
            onNameChange = { newValue ->
                inputName = newValue
            },
            inputEmail = inputEmail,
            errorEmail = errorEmail,
            onEmailChange = { newValue ->
                inputEmail = newValue
                errorEmail = !isEmailValid(inputEmail)
            },
            inputPhoneNumber = inputPhoneNumber,
            onPhoneNumberChange = { newValue ->
                inputPhoneNumber = newValue
            },
            inputPassword = inputPassword,
            errorPassword = errorPassword,
            onPasswordChange = { newValue ->
                inputPassword = newValue
                errorPassword = !isPasswordValid(inputPassword)
            },
            inputConfirmPassword = inputConfirmPassword,
            errorConfirmPassword = errorConfirmPassword,
            onConfirmPasswordChange = { newValue ->
                inputConfirmPassword = newValue
                errorConfirmPassword = !isPasswordValid(inputConfirmPassword)
            },
            navigateToLogin = {},
            registerOnClick = {},
            isEnabled = true
        )
    }

}