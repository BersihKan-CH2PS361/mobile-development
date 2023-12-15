package com.example.bersihkan.ui.screen.customer.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.local.model.UserModel
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponseItem
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.utils.UserRole
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DataRepository) : ViewModel() {

    private val _userModel: MutableStateFlow<UserModel> = MutableStateFlow(UserModel("","","",UserRole.NONE,"","","",false)) // Default value, modify as needed
    val userModel: StateFlow<UserModel> get() = _userModel
    private val _histories: MutableStateFlow<UiState<List<DetailOrderResponseItem>>> = MutableStateFlow(
        UiState.Initial
    )
    val histories: StateFlow<UiState<List<DetailOrderResponseItem>>> get() = _histories
    private val _contents: MutableStateFlow<UiState<List<ContentsResponse>>> = MutableStateFlow(UiState.Initial)
    val contents: StateFlow<UiState<List<ContentsResponse>>> get() = _contents

    private var isDataLoaded = false

    fun fetchDataIfNeeded() {
        if (!isDataLoaded) {
            // Fetch data here
            getDetailHistory()
            getContents()
            isDataLoaded = true
        }
    }

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { userModel ->
                _userModel.value = userModel
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getDetailHistory(){
        viewModelScope.launch {
            _histories.value = UiState.Loading
            repository.getDetailHistory().collect{ response ->
                Log.d("HomeViewModel", "getDetailHistory: $response")
                when(response){
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

    fun getContents(){
        viewModelScope.launch {
            _contents.value = UiState.Loading
            repository.getContents().collect{ response ->
                Log.d("HomeViewModel", "getContents: $response")
                when(response){
                    is ResultState.Success -> {
                        _contents.value = UiState.Success(response.data)
                    }
                    is ResultState.Error -> {
                        _contents.value = UiState.Error(response.error)
                    }
                }
            }
        }
    }

}