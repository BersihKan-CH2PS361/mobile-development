package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineLarge
import com.example.bersihkan.ui.theme.textMediumTiny

@Composable
fun ProfileCard(
    icon: Int,
    title: String,
    desc: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = color,
        contentColor = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(Shapes.medium)
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = subHeadlineLarge
                )
                Text(
                    text = desc,
                    style = textMediumTiny
                )
            }
            IconButton(onClick = onClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCardPreview() {
    BersihKanTheme {
        ProfileCard(
            icon = R.drawable.ic_edit_profile,
            title = "Edit Profile",
            desc = "Edit your profile account",
            color = PacificBlue,
            onClick = { }
        )
    }
}