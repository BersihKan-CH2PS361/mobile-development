package com.example.bersihkan.ui.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    query: String,
    onClick: () -> Unit,
    onLocationClicked: (LatLng) -> Unit,
    onErrorFetchData: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var searchBarClicked by remember { mutableStateOf(false) }

    val context = LocalContext.current

    var searchText by remember {
        mutableStateOf(query)
    }
    var predictionList by remember {
        mutableStateOf(emptyList<AutocompletePrediction>())
    }

    SearchBar(
        query = query,
        onQueryChange = { newValue ->
            searchText = newValue
            performAutocompleteSearch(context,
                query = searchText,
                onSearchResults = { predictions ->
                    predictionList = predictions
                },
                onError = {
                    predictionList = emptyList<AutocompletePrediction>()
                }
            )
        },
        onSearch = { newValue ->
            searchText = newValue
            performAutocompleteSearch(context,
                query = searchText,
                onSearchResults = { predictions ->
                    predictionList = predictions
                },
                onError = {
                    predictionList = emptyList<AutocompletePrediction>()
                }
            )
        },
        active = false,
        enabled = false,
        colors = SearchBarDefaults.colors(
            containerColor = Botticelli,
            inputFieldColors = TextFieldDefaults.colors(
                disabledContainerColor = Botticelli,
                disabledTextColor = Color.Black,
                disabledLeadingIconColor = BlueLagoon
            )
        ),
        onActiveChange = { },
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
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .heightIn(36.dp)
            .padding(vertical = 0.dp, horizontal = 0.dp)
    ) {
        LocationSearchResults(
            predictionsList = predictionList,
            onItemClick = { placeId ->
                fetchPlaceDetails(context, placeId,
                    onPlaceDetails = { latLng ->
                        onLocationClicked(latLng)
                    },
                    onError = {
                        Log.d("SearchScreen", "fetchPlaceDetails: ${it.message}")
                        onErrorFetchData(it.message.toString())
                    }
                )
            }
        )
    }

}

fun performAutocompleteSearch(context: Context, query: String, onSearchResults: (List<AutocompletePrediction>) -> Unit, onError: (Exception) -> Unit) {
    val placesClient = Places.createClient(context)
    val autocompleteRequest = AutocompleteSessionToken.newInstance()

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setSessionToken(autocompleteRequest)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
            val predictionsList = response.autocompletePredictions
            onSearchResults(predictionsList)
        }
        .addOnFailureListener { exception: Exception ->
            onError(exception)
        }
}

@Composable
fun LocationSearchResults(predictionsList: List<AutocompletePrediction>, onItemClick: (String) -> Unit) {
    LazyColumn {
        items(predictionsList) { prediction ->
            Text(
                text = prediction.getFullText(null).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(prediction.placeId) } // Pass placeId on item click
                    .padding(16.dp)
            )
            Divider()
        }
    }
}

fun fetchPlaceDetails(context: Context, placeId: String, onPlaceDetails: (LatLng) -> Unit, onError: (Exception) -> Unit) {
    val placesClient = Places.createClient(context)
    val placeFields = listOf(Place.Field.LAT_LNG)

    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place
            val latLng = place.latLng
            onPlaceDetails(latLng) // Pass LatLng to the callback function
        }
        .addOnFailureListener { exception: Exception ->
            onError(exception)
        }
}

@Preview
@Composable
fun SearchPreview() {
    BersihKanTheme {
        Search(
            query = "Jl.Ir Juanda No.50",
            onClick = {},
            onLocationClicked = {},
            onErrorFetchData = {},
        )
    }
}