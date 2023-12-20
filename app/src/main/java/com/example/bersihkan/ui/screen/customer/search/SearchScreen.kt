package com.example.bersihkan.ui.screen.customer.search

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bersihkan.R
import com.example.bersihkan.ui.components.modal.RegisterLoginDialog
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.Mercury
import com.example.bersihkan.ui.theme.Shapes
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

@Composable
fun SearchScreen(
    query: String,
    navigateToOrder: (Float, Float) -> Unit,
) {
    val context = LocalContext.current

    var searchText by remember {
        mutableStateOf(query)
    }
    var predictionList by remember {
        mutableStateOf(emptyList<AutocompletePrediction>())
    }

    var isErrorLocation by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Search bar
            TextField(
                value = searchText,
                onValueChange = { newValue ->
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
                shape = Shapes.small,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Mercury,
                    focusedContainerColor = Mercury
                ),
                label = { Text("Search Location") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = Shapes.small)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search results
            LocationSearchResults(predictionList) { placeId ->
                fetchPlaceDetails(context, placeId,
                    onPlaceDetails = { latLng ->
                        val lat = latLng.latitude
                        val lon = latLng.longitude
                        navigateToOrder(lat.toFloat(), lon.toFloat())
                    },
                    onError = {
                        Log.d("SearchScreen", "fetchPlaceDetails: ${it.message}")
                        isErrorLocation = true
                    }
                )
            }
        }
    }

    if (isErrorLocation) {
        RegisterLoginDialog(
            title = stringResource(R.string.error),
            message = stringResource(R.string.failed_to_load_location_data),
            onDismiss = {
                isErrorLocation = false
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

//Function to handle item click
fun onPredictionClicked(context: Context, placeId: String) {
    fetchPlaceDetails(context, placeId,
        onPlaceDetails = { latLng ->
            // Handle LatLng - latLng.latitude and latLng.longitude
        },
        onError = { /* Handle error */ }
    )
}

@Preview(showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    BersihKanTheme {
        SearchScreen(
            query = "Jl. Ir. Juanda No.50",
            navigateToOrder = { lat, lng -> }
        )
    }
}