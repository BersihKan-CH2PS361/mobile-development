package com.example.bersihkan.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.BostonBlue
import com.example.bersihkan.ui.theme.Botticelli
import com.example.bersihkan.ui.theme.Grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

//    SearchBar(
//        query = query,
//        onQueryChange = onQueryChange,
//        onSearch = { },
//        active = false,
//        onActiveChange = { },
//        leadingIcon = {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_search_bar),
//                tint = BostonBlue,
//                contentDescription = null
//            )
//        },
//        placeholder = {
//            Text(text = stringResource(R.string.search_location))
//        },
//        shape = MaterialTheme.shapes.large,
//        modifier = modifier
//            .fillMaxWidth()
//            .heightIn(36.dp)
//            .padding(vertical = 0.dp, horizontal = 0.dp)
//    ) {
//
//    }

    TextField(
        value = query,
        onValueChange = onQueryChange,
        enabled = false,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_bar),
                tint = BostonBlue,
                contentDescription = null
            )
        },
        placeholder = {
            Text(text = stringResource(R.string.search_location))
        },
        maxLines = 1,
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledContainerColor = Botticelli,
            disabledLeadingIconColor = BlueLagoon,
            focusedContainerColor = Botticelli
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(36.dp)
            .padding(vertical = 0.dp, horizontal = 0.dp)
            .clickable { onQueryChange(query) }
    )

}

@Preview
@Composable
fun SearchPreview() {
    BersihKanTheme {
        Search(query = "Jl. Juanda No. 50", onQueryChange = { })
    }
}