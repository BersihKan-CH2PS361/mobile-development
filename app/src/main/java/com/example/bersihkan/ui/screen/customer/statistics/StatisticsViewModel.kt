package com.example.bersihkan.ui.screen.customer.statistics

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(private val repository: DataRepository): ViewModel() {

    private val _histories: MutableStateFlow<UiState<List<DetailOrderResponse>>> =
        MutableStateFlow(
            UiState.Initial
        )
    val histories: StateFlow<UiState<List<DetailOrderResponse>>> get() = _histories
    private var isFirstLoadHistory = true

    fun getDetailHistory() {
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

}