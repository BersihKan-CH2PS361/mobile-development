package com.example.bersihkan.ui.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.headlineSmall
import com.example.bersihkan.ui.theme.textMediumExtraSmall

@Composable
fun LoginRegisterSection(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = headlineSmall,
            color = Color.Black
        )
        Text(
            text = description,
            style = textMediumExtraSmall,
            color = Grey
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginRegisterSectionPreview() {
    BersihKanTheme {
        LoginRegisterSection(
            title = stringResource(R.string.register),
            description = stringResource(R.string.register_desc)
        )
    }
}