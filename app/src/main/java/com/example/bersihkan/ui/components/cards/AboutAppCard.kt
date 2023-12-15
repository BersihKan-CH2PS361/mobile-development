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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.textMediumExtraSmall

@Composable
fun AboutAppCard(
    modifier: Modifier = Modifier
) {

    val text = "Bersih.kan is a waste-management app which helps people to manage their household waste by collecting and recycling it with the help of collectors. We aim to achieve SDGs 11 sustainable cities and communities to maintain sustainable waste services."


    val annotatedText = buildAnnotatedString {
        val annotatedPart = "SDGs 11 sustainable cities and communities"
        val index = text.indexOf(annotatedPart)

        if (index >= 0) {
            append(text.substring(0, index))

            withStyle(style = SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                append(annotatedPart)
            }

            append(text.substring(index + annotatedPart.length)) // Append text after the annotated part
        } else {
            append(text)
        }
    }

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(Shapes.medium)
    ){
        Box(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = annotatedText,
                style = textMediumExtraSmall,
                color = Color.Black,
                textAlign = TextAlign.Justify
            )
        }
    }

}

@Preview
@Composable
fun AboutAppCardPreview() {

    BersihKanTheme {
        AboutAppCard()
    }

}