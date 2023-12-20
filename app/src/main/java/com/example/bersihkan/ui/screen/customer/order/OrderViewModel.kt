package com.example.bersihkan.ui.screen.customer.order

import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.remote.response.GeneralResponse
import com.example.bersihkan.data.remote.response.RegisterResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.utils.WasteType
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: DataRepository): ViewModel() {

    private val _locationName: MutableStateFlow<String> = MutableStateFlow("")
    val locationName: StateFlow<String> get() = _locationName

    private var _response: MutableStateFlow<UiState<GeneralResponse>> = MutableStateFlow(UiState.Initial)
    val response: StateFlow<UiState<GeneralResponse>> get() = _response

    private val _ongoingOrder: MutableStateFlow<UiState<DetailOrderResponse>> =
        MutableStateFlow(UiState.Initial)
    val ongoingOrder: StateFlow<UiState<DetailOrderResponse>> get() = _ongoingOrder

    var lat = mutableFloatStateOf(0f)
    var lon = mutableFloatStateOf(0f)
    var wasteType = mutableStateOf(WasteType.INITIAL)
    var wasteQty = mutableIntStateOf(0)
    var wasteFee = wasteQty.intValue.times(wasteType.value.price)
    var subtotalFee = wasteFee.plus(12000)
    var notes = mutableStateOf("")
    var isEnabled = wasteType.value != WasteType.INITIAL && wasteQty.intValue != 0 && wasteFee != 0 && notes.value != ""

    fun getLocationName() {
        Log.d("HomeViewModel", "lat: ${lat.floatValue}, lon: ${lon.floatValue}")
        viewModelScope.launch {
            _locationName.value = repository.getAddressFromLatLng(lat.floatValue, lon.floatValue)
        }
    }

    fun createOrder(){
        Log.d("OrderViewModal", "wasteFee: $wasteFee")
        Log.d("OrderViewModal", "subtotalFee: $subtotalFee")
        viewModelScope.launch {
            _response.value = UiState.Loading
            repository.createOrder(
                pickupLat = lat.floatValue,
                pickupLon = lon.floatValue,
                wasteType = wasteType.value,
                wasteQty = wasteQty.intValue,
                userNotes = notes.value,
                pickupFee = 12000,
                recycleFee = wasteFee,
                subtotalFee = subtotalFee
            ).collect{ resultState ->
                Log.d("OrderViewModel", "$resultState")
                when(resultState){
                    is ResultState.Success -> {
                        _response.value = UiState.Success(resultState.data)
                        repository.getCurrentOrderUser().collect { response ->
                            Log.d("HomeViewModel", "getCurrentOrderUser: $response")
                            when (response) {
                                is ResultState.Success -> {
                                    val order = response.data.first()
                                    _ongoingOrder.value = UiState.Success(order)
                                }

                                is ResultState.Error -> {
                                    _ongoingOrder.value = UiState.Error(response.error)
                                }
                            }
                        }
                    }
                    is ResultState.Error -> {
                        _response.value = UiState.Error(resultState.error)
                    }
                }
            }
        }
    }

    fun refreshCount(){
        wasteFee = wasteQty.intValue.times(wasteType.value.price)
        subtotalFee = wasteFee.plus(12000)
        isEnabled = wasteType.value != WasteType.INITIAL && wasteQty.intValue != 0 && wasteFee != 0 && notes.value != ""
    }

    fun getCurrentOrderUser() {
        viewModelScope.launch {
            _ongoingOrder.value = UiState.Loading
            repository.getCurrentOrderUser().collect { response ->
                Log.d("HomeViewModel", "getCurrentOrderUser: $response")
                when (response) {
                    is ResultState.Success -> {
                        val order = response.data.first()
                        _ongoingOrder.value = UiState.Success(order)
                    }

                    is ResultState.Error -> {
                        _ongoingOrder.value = UiState.Error(response.error)
                    }
                }
            }
        }
    }

}