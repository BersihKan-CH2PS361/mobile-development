package com.example.bersihkan.ui.screen.customer.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.local.model.UserModel
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.utils.UserRole
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private var isFirstLoadHistory = true
    private var isFirstLoadContents = true
    private var isFirstLoadOrder = true

    fun refreshData() {
        viewModelScope.launch {
            while (true) { // Continuously fetch data
                // Fetch data here
                getDetailHistory()
                getContents()
                getCurrentOrderUser()

                delay(30000)
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
                        val order = response.data.first()
                        _ongoingOrder.value = UiState.Success(order)
                        isFirstLoadOrder = false
                    }

                    is ResultState.Error -> {
                        _ongoingOrder.value = UiState.Error(response.error)
                        isFirstLoadOrder = false
                    }
                }
            }
        }
    }

}