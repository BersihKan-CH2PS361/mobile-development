package com.example.bersihkan.ui.screen.collector.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.bersihkan.R
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.data.local.DataDummy
import com.example.bersihkan.data.remote.response.DetailCollectorResponse
import com.example.bersihkan.data.remote.response.DetailUserResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.ui.components.cards.ProfileCard
import com.example.bersihkan.ui.components.modal.ConfirmDialog
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.bersihkan.ui.screen.customer.profile.ProfileContent
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Red
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.gradient1
import com.example.bersihkan.ui.theme.headlineExtraSmall
import com.example.bersihkan.ui.theme.headlineSmall
import com.example.bersihkan.ui.theme.textMediumMedium
import com.example.bersihkan.ui.theme.textMediumSmall
import com.example.kekkomiapp.ui.common.UiState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bersihkan.data.remote.response.DetailCollectorByIdResponse

@Composable
fun ProfileCollectorScreen(
    navigateToEditProfile: () -> Unit,
    navigateToAbout: () -> Unit,
    navigateToLogout: () -> Unit,
    viewModel: ProfileCollectorViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    viewModel.getSession()
    LaunchedEffect(key1 = viewModel, block = {
        viewModel.getUserData()
    })

    viewModel.user.collectAsState().value.let { userData ->
        when(userData){
            is UiState.Initial -> {}
            is UiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier.fillMaxSize()
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
                ProfileCollectorContent(
                    user = userData.data,
                    navigateToEditProfile = navigateToEditProfile,
                    navigateToAbout = navigateToAbout,
                    navigateToLogout = {
                        viewModel.logout()
                        navigateToLogout()
                    },
                    modifier = modifier
                )
            }
            is UiState.Error -> {
                ProfileCollectorContent(
                    user = DetailCollectorByIdResponse(),
                    navigateToEditProfile = { },
                    navigateToAbout = navigateToAbout,
                    navigateToLogout = {
                        viewModel.logout()
                        navigateToLogout()
                    },
                    modifier = modifier
                )
            }
        }
    }

    viewModel.logout.collectAsState().value.let { data ->
        data.getContentIfNotHandled().let { logout ->
            when(logout){
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
                    Toast.makeText(LocalContext.current, "You're logged out", Toast.LENGTH_SHORT).show()
                    navigateToLogout()
                }
                is UiState.Error -> {
                    Toast.makeText(LocalContext.current, "Failed to logged out: ${logout.errorMsg}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

}

@Composable
fun ProfileCollectorContent(
    user: DetailCollectorByIdResponse,
    isError: Boolean = false,
    textError: String = "",
    navigateToEditProfile: () -> Unit,
    navigateToAbout: () -> Unit,
    navigateToLogout: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isDialogShowed by remember {
        mutableStateOf(true)
    }

    if(!isDialogShowed){
        ConfirmDialog(
            title = stringResource(id = R.string.logout),
            message = stringResource(R.string.logout_check),
            onPositive = navigateToLogout,
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
                .height(590.dp)
                .align(Alignment.BottomCenter)
        ) {
        }
        if(isError){
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
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
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
                        text = user.collectorName.toString(),
                        style = headlineSmall.copy(
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    )
                    Text(
                        text = stringResource(
                            R.string.collector_profile,
                            user.email.toString(),
                            user.facilityName.toString()
                        ),
                        style = textMediumSmall.copy(
                            color = Grey,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 30.dp)
                ) {
                    ProfileCard(
                        icon = R.drawable.ic_edit_profile,
                        title = stringResource(R.string.edit_profile),
                        desc = stringResource(R.string.edit_profile_desc),
                        color = PacificBlue,
                        onClick = navigateToEditProfile,
                        modifier = Modifier.clickable {
                            navigateToEditProfile()
                        }
                    )
                    ProfileCard(
                        icon = R.drawable.ic_info,
                        title = stringResource(R.string.about),
                        desc = stringResource(R.string.about_desc),
                        color = Java,
                        onClick = navigateToAbout,
                        modifier = Modifier.clickable {
                            navigateToAbout()
                        }
                    )
                }
                Text(
                    text = stringResource(R.string.logout),
                    style = headlineExtraSmall.copy(
                        fontSize = 16.sp,
                        color = Red,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            isDialogShowed = false
                        }
                )
            }
        }
    }
    
}

@Preview(showSystemUi = true)
@Composable
fun ProfileCollectorContentPreview() {
    BersihKanTheme {
        ProfileCollectorContent(
            user = DetailCollectorByIdResponse(),
            navigateToEditProfile = { /*TODO*/ },
            navigateToAbout = { /*TODO*/ },
            navigateToLogout = { /*TODO*/ })
    }
}