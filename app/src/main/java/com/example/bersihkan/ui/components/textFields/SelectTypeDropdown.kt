package com.example.bersihkan.ui.components.textFields

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import com.example.bersihkan.ui.theme.Grey
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineExtraSmall
import com.example.bersihkan.ui.theme.textMediumMedium
import com.example.bersihkan.utils.WasteType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTypeDropdown(
    selectedItem: WasteType,
    onItemSelected: (WasteType) -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.waste_type),
            style = subHeadlineExtraSmall,
            color = Grey
        )
        Box(
            modifier = Modifier
                .size(184.dp, 44.dp)
                .border(
                    width = 2.dp,
                    color = if (expanded) BlueLagoon else Color.Gray,
                    shape = MaterialTheme.shapes.medium
                )
                .clickable(interactionSource = interactionSource, indication = null) {
                    expanded = true
                }
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedItem.type,
                    style = textMediumMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
                IconButton(onClick = {
                        expanded = !expanded
                    },
                ) {
                    Icon(
                        painter = painterResource(id = if(expanded) R.drawable.ic_dropdown_true else R.drawable.ic_dropdown_false),
                        contentDescription = null
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(185.dp)
                    .clip(Shapes.medium)
            ) {
                WasteType.values().filter { it != WasteType.INITIAL }.forEach { wasteType ->
                    DropdownMenuItem(
                        text = {
                            Text(text = wasteType.type)
                        },
                        onClick = {
                            onItemSelected(wasteType)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectTypeDropdownPreview() {
    var selectedItem by remember {
        mutableStateOf(WasteType.INITIAL)
    }
    BersihKanTheme {
        SelectTypeDropdown(
            selectedItem = selectedItem,
            onItemSelected = { newItem ->
                selectedItem = newItem
            }
        )
    }
}