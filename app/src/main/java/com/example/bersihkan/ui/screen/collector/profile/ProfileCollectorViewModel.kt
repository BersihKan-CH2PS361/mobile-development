package com.example.bersihkan.ui.screen.collector.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.DetailCollectorByIdResponse
import com.example.bersihkan.data.remote.response.DetailCollectorResponse
import com.example.bersihkan.data.remote.response.DetailUserResponse
import com.example.bersihkan.data.remote.response.GeneralResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.utils.Event
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileCollectorViewModel(private val repository: DataRepository): ViewModel() {

    private val _user: MutableStateFlow<UiState<DetailCollectorByIdResponse>> = MutableStateFlow(UiState.Initial)
    val user: StateFlow<UiState<DetailCollectorByIdResponse>> get() = _user
    private val _logout: MutableStateFlow<Event<UiState<GeneralResponse>>> = MutableStateFlow(
        Event(
            UiState.Initial)
    )
    val logout: StateFlow<Event<UiState<GeneralResponse>>> get() = _logout
    private var id = mutableStateOf("")
    var isDialogShowed: Boolean = false

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { userModel ->
                id.value = userModel.id
            }
        }
    }

    fun getUserData(){
        viewModelScope.launch {
            _user.value = UiState.Loading
            repository.getDetailCollectorById().collect{ detailUser ->
                when(detailUser){
                    is ResultState.Success -> {
                        _user.value = UiState.Success(detailUser.data)
                    }
                    is ResultState.Error -> {
                        _user.value = UiState.Error(detailUser.error)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logout.value = Event(UiState.Loading)
            repository.logout().collect{ data ->
                when(data){
                    is ResultState.Success -> {
                        _logout.value = Event(UiState.Success(data.data))
                    }
                    is ResultState.Error -> {
                        _logout.value = Event(UiState.Error(data.error))
                    }
                    else -> {}
                }
            }
        }
    }

}