package com.example.bersihkan.ui.screen.customer.home

import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DataRepository) : ViewModel() {

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
    private val _contents: MutableStateFlow<UiState<List<ContentsResponse>>> =
        MutableStateFlow(UiState.Initial)
    val contents: StateFlow<UiState<List<ContentsResponse>>> get() = _contents
    private val _ongoingOrder: MutableStateFlow<UiState<DetailOrderResponse>> =
        MutableStateFlow(UiState.Initial)
    val ongoingOrder: StateFlow<UiState<DetailOrderResponse>> get() = _ongoingOrder
    private val _locationName: MutableStateFlow<String> = MutableStateFlow("")
    val locationName: StateFlow<String> get() = _locationName
    private val ongoingOrderId: MutableStateFlow<Int> = MutableStateFlow(-1)
    private val _orderStatus: MutableStateFlow<OrderStatus> = MutableStateFlow(OrderStatus.INITIAL)
    val orderStatus: StateFlow<OrderStatus> get() = _orderStatus
    private var _notification: MutableStateFlow<Event<Boolean>> = MutableStateFlow(Event(false))
    val notification: StateFlow<Event<Boolean>> get() = _notification
    private var _isEnable: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isEnable: StateFlow<Boolean> get() = _isEnable
    private var _isSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching
    var isErrorLocationShowed = mutableStateOf(false)

    var lat = mutableFloatStateOf(0f)
    var lon = mutableFloatStateOf(0f)

    private var isFirstLoadHistory = true
    private var isFirstLoadContents = true
    private var isFirstLoadOrder = true

    fun isNowSearching(search: Boolean){
        _isSearching.value = search
        Log.d("HomeViewModel", "isSearching: $search")
    }

    fun refreshData() {
        viewModelScope.launch {
            while (true) { // Continuously fetch data
                // Fetch data here
                getDetailHistory()
                getContents()
                getCurrentOrderUser()

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

    private fun getDetailHistory() {
        viewModelScope.launch {
            if(isFirstLoadHistory) _histories.value = UiState.Loading
            repository.getDetailHistory().collect { response ->
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

    private fun getContents() {
        viewModelScope.launch {
            if(isFirstLoadContents) _contents.value = UiState.Loading
            repository.getContents().collect { response ->
                Log.d("HomeViewModel", "getContents: $response")
                when (response) {
                    is ResultState.Success -> {
                        _contents.value = UiState.Success(response.data)
                        isFirstLoadContents = false
                    }

                    is ResultState.Error -> {
                        _contents.value = UiState.Error(response.error)
                        isFirstLoadContents = false
                    }
                }
            }
        }
    }

    private fun getCurrentOrderUser() {
        viewModelScope.launch {
            if(isFirstLoadOrder) _ongoingOrder.value = UiState.Loading
            repository.getCurrentOrderUser().collect { response ->
                Log.d("HomeViewModel", "getCurrentOrderUser: $response")
                when (response) {
                    is ResultState.Success -> {
                        _isEnable.value = false
                        val order = response.data.first()
                        checkOrderStatusChange(findOrderStatus(order.orderStatus.toString()))
                        ongoingOrderId.value = order.orderId?.toInt() ?: -1
                        _ongoingOrder.value = UiState.Success(order)
                        isFirstLoadOrder = false
                    }

                    is ResultState.Error -> {
                        _isEnable.value = true
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
                }
            }
        }
    }

    fun getLocationName() {
        Log.d("HomeViewModel", "lat: ${lat.value}, lon: ${lon.value}")
        viewModelScope.launch {
            _locationName.value = repository.getAddressFromLatLng(lat.value, lon.value)
        }
    }

    private fun checkOrderStatusChange(newStatus: OrderStatus) {
        _notification.value = Event(false)
        if (newStatus != _orderStatus.value) {
            _orderStatus.value = newStatus
            _notification.value = Event(true)
        }
    }

}