package com.example.bersihkan.ui.screen.general.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.components.cards.AboutAppCard
import com.example.bersihkan.ui.components.cards.OurStoryCard
import com.example.bersihkan.ui.components.section.HomeSection
import com.example.bersihkan.ui.components.topBar.TopBar
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Mercury
import com.example.bersihkan.ui.theme.gradient1
import com.example.bersihkan.ui.theme.subHeadlineSmall
import com.example.bersihkan.ui.theme.textMediumSmall

@Composable
fun AboutScreen(
    navigateToBack: () -> Unit
) {

    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier
            .fillMaxSize()
            .background(gradient1(800f))
    ){
        item {
            TopBar(
                text = stringResource(R.string.about),
                onBackClick = navigateToBack,
                enableOnBack = true,
                color = Color.White
            )
        }
        item {
            HomeSection(
                title = stringResource(R.string.about_our_app),
                color = Color.White,
                content = {
                    AboutAppCard()
                },
                navigateToStatistic = {},
                modifier = Modifier.padding(20.dp)
            )
        }
        item {
            HomeSection(
                title = stringResource(R.string.our_story),
                color = Color.White,
                content = {
                    OurStoryCard()
                },
                navigateToStatistic = { },
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(100.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_version),
                    style = subHeadlineSmall.copy(
                        color = Mercury
                    ),
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun AboutScreenPreview() {
    BersihKanTheme {
        AboutScreen(navigateToBack = {})
    }
}