package com.example.bersihkan.ui.screen.customer.delivery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.DetailOrderAll
import com.example.bersihkan.data.repository.DataRepository
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeliveryViewModel(private val repository: DataRepository) : ViewModel() {

    private val _orderData: MutableStateFlow<UiState<DetailOrderAll>> =
        MutableStateFlow(
            UiState.Initial
        )
    val orderData: StateFlow<UiState<DetailOrderAll>> get() = _orderData

    fun getDetailHistoryById(id: Int) {
        viewModelScope.launch {
            _orderData.value = UiState.Loading
            repository.getAllDetailOrder(id).collect { response ->
                Log.d("HomeViewModel", "getDetailOrderById: $response")
                when (response) {
                    is ResultState.Success -> {
                        val order = response.data
                        Log.d("DetailViewModel", "detailOrderAll: ${_orderData.value}")
                        _orderData.value = UiState.Success(order)
                    }

                    is ResultState.Error -> {
                        _orderData.value = UiState.Error(response.error)
                    }
                }
            }
        }
    }

}