package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.helper.formatToRupiah
import com.example.bersihkan.ui.components.buttons.OrderNowButton
import com.example.bersihkan.ui.components.Search
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Botticelli
import com.example.bersihkan.ui.theme.gradient2
import com.example.bersihkan.ui.theme.headlineExtraLarge
import com.example.bersihkan.ui.theme.headlineExtraSmall
import com.example.bersihkan.ui.theme.headlineLarge
import com.example.bersihkan.ui.theme.textMediumLarge

@Composable
fun CurrentBalanceCard(
    currentBalance: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(145.dp)
    ){
        Surface(
            color = Color.Transparent,
            contentColor = Color.White,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp,
                        topEnd = 30.dp
                    )
                )
                .height(145.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = gradient2(1500f, 175f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.your_earnings),
                        style = headlineExtraSmall,
                    )
                    Text(
                        text = stringResource(R.string.current_balance),
                        style = textMediumLarge,
                    )
                    Text(
                        text = formatToRupiah(currentBalance),
                        style = headlineLarge.copy(
                            color = Color.White
                        ),
                    )
//                    Search(
//                        query = query,
//                        onQueryChange = onQueryChange,
//                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White)
                .align(Alignment.TopEnd)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Botticelli)
                    .align(Alignment.Center)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sustainability),
                    contentDescription = null,
                    tint = BlueLagoon,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentBalanceCardPreview() {
    BersihKanTheme {
        CurrentBalanceCard(2000000)
    }
}