package com.example.bersihkan.ui.screen.collector.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryCollectorViewModel(private val repository: DataRepository) : ViewModel() {

    private val _histories: MutableStateFlow<UiState<List<DetailOrderResponse>>> =
        MutableStateFlow(
            UiState.Initial
        )
    val histories: StateFlow<UiState<List<DetailOrderResponse>>> get() = _histories

    fun getDetailHistoryCollector() {
        viewModelScope.launch {
            _histories.value = UiState.Loading
            repository.getDetailHistoryCollector().collect { response ->
                Log.d("HomeViewModel", "getDetailHistoryCollector: $response")
                when (response) {
                    is ResultState.Success -> {
                        _histories.value = UiState.Success(response.data)
                    }

                    is ResultState.Error -> {
                        _histories.value = UiState.Error(response.error)
                    }
                }
            }
        }
    }

}