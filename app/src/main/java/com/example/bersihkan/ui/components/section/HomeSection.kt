package com.example.bersihkan.ui.components.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.subHeadlineExtraSmall

@Composable
fun HomeSection(
    title: String,
    content: @Composable () -> Unit,
    isStatistic: Boolean = false,
    navigateToStatistic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionText(title, Modifier.weight(1f).padding(bottom = 10.dp))
            if(isStatistic){
                Text(
                    text = stringResource(R.string.see_all),
                    style = subHeadlineExtraSmall.copy(
                        color = Grey,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.padding(end = 20.dp).clickable { navigateToStatistic() }
                )
            }
        }
        content()
    }
}