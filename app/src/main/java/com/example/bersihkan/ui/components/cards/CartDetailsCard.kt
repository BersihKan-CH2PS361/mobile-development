package com.example.bersihkan.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BonJour
import com.example.bersihkan.ui.theme.Cerulean
import com.example.bersihkan.ui.theme.Java
import com.example.bersihkan.ui.theme.PacificBlue
import com.example.bersihkan.ui.theme.Shapes
import com.example.bersihkan.ui.theme.subHeadlineSmall
import com.example.bersihkan.ui.theme.textRegularExtraSmall
import com.example.bersihkan.utils.WasteType

@Composable
fun CartDetailsCard(
    pickup: String,
    wasteType: String,
    wasteQty: Int,
    notes: String,
    modifier: Modifier = Modifier
) {

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .clip(Shapes.large)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            CardSection(
                title = stringResource(R.string.pick_up),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = "Location",
                            tint = PacificBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = pickup,
                            style = textRegularExtraSmall.copy(
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            )
            CardSection(
                title = stringResource(R.string.waste_details),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_type),
                                contentDescription = "Waste Type",
                                tint = Cerulean,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if(wasteType != WasteType.INITIAL.type) wasteType else "-",
                                style = textRegularExtraSmall.copy(
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                ),
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_weight),
                                contentDescription = "Weight",
                                tint = Java,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if(wasteQty > 0) wasteQty.toString() else "0",
                                style = textRegularExtraSmall.copy(
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                ),
                            )
                        }
                    }
                }
            )
            CardSection(
                title = stringResource(R.string.notes),
                content = {
                    Text(
                        text = if(notes.isNotEmpty() || notes != "") notes else "-",
                        style = textRegularExtraSmall.copy(
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                        ),
                        minLines = 5,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .verticalScroll(rememberScrollState())
                    )
                },
                showDivider = false
            )
        }
    }
}

@Composable
fun CardSection(
    title: String,
    content: @Composable () -> Unit,
    showDivider: Boolean = true,
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = subHeadlineSmall,
            color = Color.Black,
        )
        content()
        if(showDivider){
            Divider(
                thickness = 1.dp,
                color = BonJour,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
            )
        }
    }
    
}

@Preview
@Composable
fun CartDetailsCardPreview() {
    BersihKanTheme {
        CartDetailsCard(
            pickup = "Jl. Ir Juanda No.50",
            wasteType = WasteType.PAPER.type,
            wasteQty = 5,
            notes = "Lorem ipsum dolor sit amet consectetur. Lorem pharetra at viverra nibh sed quisque non odio faucibus. Ut sit lobortis purus pellentesque. Ut bibendum vel sed nibh morbi. Habitant integer arcu eget turpis leo amet arcu semper. Aliquet venenatis vulputate vitae nisl iaculis sit. Morbi imperdiet ipsum et magna eget venenatis eget diam. Quis eu posuere porta nibh sem at turpis vel elementum. In augue morbi purus diam massa. Dignissim tempus turpis massa vel sed leo in donec."
        )
    }
}