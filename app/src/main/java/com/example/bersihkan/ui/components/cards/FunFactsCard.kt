package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueChill
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.headlineExtraSmall
import com.example.bersihkan.ui.theme.textRegularExtraTiny

@Composable
fun FunFactsCard(
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = BlueChill,
        contentColor = Color.White,
        modifier = modifier
            .size(250.dp, 128.dp)
            .clip(Shapes.large)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = headlineExtraSmall,
                maxLines = 2
            )
            Text(
                text = desc,
                style = textRegularExtraTiny,
                maxLines = 5,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview
@Composable
fun FunFactsCardPreview() {
    BersihKanTheme {
        FunFactsCard(
            title = "Lorem ipsum dolor sit amet consectetur.",
            desc = "Lorem ipsum dolor sit amet consectetur. Tincidunt neque cras vitae dolor faucibus nunc ac. Condimentum amet tristique fringilla pulvinar nunc netus. Suscipit aliquam sed proin pellentesque. Suscipit aliquam sed proin pellentesque.")
    }
}