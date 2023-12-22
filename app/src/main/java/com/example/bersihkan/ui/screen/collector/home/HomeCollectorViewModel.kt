package com.example.bersihkan.ui.screen.collector.home

import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.local.model.UserModel
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.helper.findOrderStatus
import com.example.bersihkan.utils.Event
import com.example.bersihkan.utils.OrderStatus
import com.example.bersihkan.utils.UserRole
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeCollectorViewModel(private val repository: DataRepository) : ViewModel() {

    private val _userModel: MutableStateFlow<UserModel> = MutableStateFlow(
        UserModel(
            "",
            "",
            "",
            UserRole.NONE,
            "",
            "",
            "",
            false
        )
    ) // Default value, modify as needed
    val userModel: StateFlow<UserModel> get() = _userModel
    private val _histories: MutableStateFlow<UiState<List<DetailOrderResponse>>> =
        MutableStateFlow(
            UiState.Initial
        )
    val histories: StateFlow<UiState<List<DetailOrderResponse>>> get() = _histories
    private val _ongoingOrder: MutableStateFlow<UiState<DetailOrderResponse>> =
        MutableStateFlow(UiState.Initial)
    val ongoingOrder: StateFlow<UiState<DetailOrderResponse>> get() = _ongoingOrder
    private val ongoingOrderId: MutableStateFlow<Int> = MutableStateFlow(-1)
    private val _orderStatus: MutableStateFlow<OrderStatus> = MutableStateFlow(OrderStatus.INITIAL)
    val orderStatus: StateFlow<OrderStatus> get() = _orderStatus
    private var _notification: MutableStateFlow<Event<Boolean>> = MutableStateFlow(Event(false))
    val notification: StateFlow<Event<Boolean>> get() = _notification

    private var isFirstLoadHistory = true
    private var isFirstLoadOrder = true

    fun refreshData() {
        viewModelScope.launch {
            while (true) { // Continuously fetch data
                // Fetch data here
                getDetailHistoryCollector()
                getCurrentOrderCollector()

                delay(15000)
            }
        }
    }

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { userModel ->
                _userModel.value = userModel
            }
        }
    }

    private fun getDetailHistoryCollector() {
        viewModelScope.launch {
            if(isFirstLoadHistory) _histories.value = UiState.Loading
            repository.getDetailHistoryCollector().collect { response ->
                Log.d("HomeViewModel", "getDetailHistory: $response")
                when (response) {
                    is ResultState.Success -> {
                        _histories.value = UiState.Success(response.data)
                        isFirstLoadHistory = false
                    }

                    is ResultState.Error -> {
                        _histories.value = UiState.Error(response.error)
                        isFirstLoadHistory = false
                    }
                }
            }
        }
    }

    private fun getCurrentOrderCollector() {
        viewModelScope.launch {
            if(isFirstLoadOrder) _ongoingOrder.value = UiState.Loading
            repository.getCurrentOrderCollector().collect { response ->
                Log.d("HomeViewModel", "getCurrentOrderCollector: $response")
                when (response) {
                    is ResultState.Success -> {
                        val order = response.data.first()
                        ongoingOrderId.value = order.orderId?.toInt() ?: -1
                        val currentOrderStatus = findOrderStatus(order.orderStatus.toString())
                        if(currentOrderStatus == OrderStatus.PICK_UP) checkOrderStatusChange(currentOrderStatus)
                        _ongoingOrder.value = UiState.Success(order)
                        isFirstLoadOrder = false
                    }

                    is ResultState.Error -> {
                        _ongoingOrder.value = UiState.Error(response.error)
                        isFirstLoadOrder = false
                        repository.getDetailOrderById(orderId = ongoingOrderId.value).collect{ order ->
                            when(order){
                                is ResultState.Success -> {
                                    val data = order.data.first()
                                    checkOrderStatusChange(findOrderStatus(data.orderStatus.toString()))
                                }
                                else -> {}
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun checkOrderStatusChange(newStatus: OrderStatus) {
        _notification.value = Event(false)
        if (newStatus != _orderStatus.value && newStatus != OrderStatus.DELIVERED) {
            _orderStatus.value = newStatus
            _notification.value = Event(true)
        }
    }

}