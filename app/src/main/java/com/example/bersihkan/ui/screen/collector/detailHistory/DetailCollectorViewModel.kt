package com.example.bersihkan.ui.screen.collector.detailHistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.DetailOrderAll
import com.example.bersihkan.data.remote.response.DetailOrderCollectorAll
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailCollectorViewModel(private val repository: DataRepository) : ViewModel() {

    private val _histories: MutableStateFlow<UiState<DetailOrderCollectorAll>> =
        MutableStateFlow(
            UiState.Initial
        )
    val histories: StateFlow<UiState<DetailOrderCollectorAll>> get() = _histories

    fun getDetailHistoryById(id: Int) {
        viewModelScope.launch {
            _histories.value = UiState.Loading
            repository.getAllDetailOrderCollector(id).collect { response ->
                Log.d("HomeViewModel", "getDetailOrderById: $response")
                when (response) {
                    is ResultState.Success -> {
                        val order = response.data
                        Log.d("DetailViewModel", "detailOrderAll: ${_histories.value}")
                        _histories.value = UiState.Success(order)
                    }

                    is ResultState.Error -> {
                        _histories.value = UiState.Error(response.error)
                    }
                }
            }
        }
    }

}