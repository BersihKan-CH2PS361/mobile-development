package com.example.bersihkan.ui.screen.general.welcomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bersihkan.R
import com.example.bersihkan.ui.components.buttons.LargeButton
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.headlineLarge
import com.example.bersihkan.ui.theme.headlineMedium
import com.example.bersihkan.ui.theme.subHeadlineLarge
import com.example.bersihkan.ui.theme.textRegularSmall

@Composable
fun WelcomePageOne(
    navigateToNextScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "BersihKan App Logo",
                modifier = Modifier
                    .size(50.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.welcome_photo_1),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
            )
        }
        Text(
            text = stringResource(R.string.welcome_to),
            color = Color.Black,
            style = headlineMedium
        )
        Text(
            text = stringResource(R.string.app_name_welcome),
            color = Java,
            style = headlineLarge.copy(
                fontSize = 36.sp
            )
        )
        Divider(
            thickness = 2.dp,
            color = BlueLagoon,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 20.dp)
                .width(100.dp)
        )
        Text(
            text = stringResource(R.string.welcome_screen_1_sub),
            color = Color.Black,
            style = subHeadlineLarge,
            modifier = Modifier.width(250.dp)
        )
        Column {
            Text(
                text = stringResource(R.string.welcome_screen_1_desc),
                color = Color.Black,
                style = textRegularSmall,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 14.dp)
            )
            LargeButton(
                text = stringResource(R.string.get_started),
                onClick = navigateToNextScreen,
                color = BlueLagoon,
                isEnabled = true,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun WelcomePageOnePreview() {

    BersihKanTheme {
        WelcomePageOne(
            navigateToNextScreen = {}
        )
    }

}