package com.example.bersihkan.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueChill
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.fontFamily

@Composable
fun StatisticsItem(
    icon: Int,
    count: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Surface(
            color = BlueChill,
            contentColor = Color.White,
            modifier = Modifier.clip(Shapes.small)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .size(24.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = count.toString(),
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                ),
            )
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsItemPreview() {
    BersihKanTheme {
        StatisticsItem(
            icon = R.drawable.ic_location,
            count = 2,
            title = "Location"
        )
    }
}