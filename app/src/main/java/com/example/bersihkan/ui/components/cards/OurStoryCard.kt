package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.textMediumExtraSmall

@Composable
fun OurStoryCard(
    modifier: Modifier = Modifier
) {

    val text = "Coming from different hometowns, our team was personally touched by the waste-related challenges we encountered from landfill closures to city inconveniences, flooding, and polluted rivers. We also witnessed the efforts of waste pickers trying to make a living. These experiences fueled our shared concern and inspired us to create a solution: a waste management app that connects those with expertise in waste management to those in need of disposal, promoting sustainability, and addressing these pressing issues."

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(Shapes.medium)
    ){
        Box(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = text,
                style = textMediumExtraSmall,
                color = Color.Black,
                textAlign = TextAlign.Justify
            )
        }
    }

}

@Preview
@Composable
fun OurStoryCardPreview() {

    BersihKanTheme {
        OurStoryCard()
    }

}