package com.example.bersihkan.ui.screen.collector.delivery

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.DetailOrderAll
import com.example.bersihkan.data.remote.response.DetailOrderCollectorAll
import com.example.bersihkan.data.remote.response.GeneralResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.helper.findOrderStatus
import com.example.bersihkan.utils.OrderStatus
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DeliveryCollectorViewModel(private val repository: DataRepository) : ViewModel() {

    private val _orderData: MutableStateFlow<UiState<DetailOrderCollectorAll>> =
        MutableStateFlow(
            UiState.Initial
        )
    val orderData: StateFlow<UiState<DetailOrderCollectorAll>> get() = _orderData
    private var _response: MutableStateFlow<UiState<GeneralResponse>> = MutableStateFlow(UiState.Initial)
    val response: StateFlow<UiState<GeneralResponse>> get() = _response
    private val _orderStatus: MutableStateFlow<OrderStatus> = MutableStateFlow(OrderStatus.INITIAL)
    val orderStatus: StateFlow<OrderStatus> get() = _orderStatus
    var selectedPict = mutableStateOf("")
    var isEnabled = if(_orderStatus.value != OrderStatus.DELIVERED) selectedPict.value != "" else false

    fun getDetailHistoryById(id: Int) {
        viewModelScope.launch {
            _orderData.value = UiState.Loading
            repository.getAllDetailOrderCollector(id).collect { response ->
                Log.d("HomeViewModel", "getDetailOrderById: $response")
                when (response) {
                    is ResultState.Success -> {
                        val order = response.data
                        Log.d("DetailViewModel", "detailOrderAll: ${_orderData.value}")
                        _orderStatus.value = findOrderStatus(order.detailOrderResponse?.orderStatus.toString())
                        _orderData.value = UiState.Success(order)
                    }

                    is ResultState.Error -> {
                        _orderData.value = UiState.Error(response.error)
                    }
                }
            }
        }
    }

    fun getNextOrderStatus(): OrderStatus? {
        return when (_orderStatus.value) {
            OrderStatus.INITIAL -> OrderStatus.PICK_UP
            OrderStatus.PICK_UP -> OrderStatus.ARRIVED
            OrderStatus.ARRIVED -> OrderStatus.DELIVERING
            OrderStatus.DELIVERING -> OrderStatus.DELIVERED
            OrderStatus.DELIVERED -> null // There's no next status after 'Delivered'
        }
    }

    fun updateOrderStatus(orderId: Int){
        viewModelScope.launch {
            _response.value = UiState.Loading
            repository.updateOrderStatus(orderId, getNextOrderStatus() ?: orderStatus.value).collect{ response ->
                when(response){
                    is ResultState.Success -> _response.value = UiState.Success(response.data)
                    is ResultState.Error -> _response.value = UiState.Error(response.error)
                }
            }
        }
    }

    fun refreshData(){
        isEnabled = if(_orderStatus.value != OrderStatus.DELIVERED) selectedPict.value != "" else false
    }

}