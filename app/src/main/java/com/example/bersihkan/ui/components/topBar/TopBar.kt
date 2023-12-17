package com.example.bersihkan.ui.components.topBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.headlineMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    text: String,
    color: Color = Color.Black,
    enableOnBack: Boolean = false,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = text,
                style = headlineMedium.copy(
                    textAlign = TextAlign.Center,
                    color = color
                )
            )
        },
        navigationIcon = {
            if(enableOnBack){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = color,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.Black
        ),
        modifier = modifier
    )
}