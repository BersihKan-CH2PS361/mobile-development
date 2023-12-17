package com.example.bersihkan.ui.screen.customer.editProfile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.DetailUserResponse
import com.example.bersihkan.data.remote.response.GeneralResponse
import com.example.bersihkan.data.remote.response.LoginResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repository: DataRepository) : ViewModel() {

    private val _user: MutableStateFlow<UiState<DetailUserResponse>> = MutableStateFlow(UiState.Initial)
    val user: StateFlow<UiState<DetailUserResponse>> get() = _user

    private var _response: MutableStateFlow<UiState<GeneralResponse>> = MutableStateFlow(UiState.Initial)
    val response: StateFlow<UiState<GeneralResponse>> get() = _response
    private var id = mutableStateOf("")

    var inputName = mutableStateOf("")
    var inputPhone = mutableStateOf("")

    private var _isEnabled: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    val isEnabled get() = _isEnabled

    fun enableButton() {
        _isEnabled.value = inputName.value != "" && inputPhone.value != ""
    }

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
            repository.getDetailUserById().collect{ detailUser ->
                when(detailUser){
                    is ResultState.Success -> {
                        inputName.value = detailUser.data.name.toString()
                        inputPhone.value = detailUser.data.phone.toString()
                        enableButton()
                        _user.value = UiState.Success(detailUser.data)
                    }
                    is ResultState.Error -> {
                        _user.value = UiState.Error(detailUser.error)
                    }
                }
            }
        }
    }

    fun saveProfile(){
        viewModelScope.launch {
            _response.value = UiState.Loading
            repository.updateProfileUser(
                name = inputName.value,
                phone = inputPhone.value
            ).collect{ resultState ->
                Log.d("LoginViewModel", "$resultState")
                when(resultState){
                    is ResultState.Success -> {
                        _response.value = UiState.Success(resultState.data)
                    }
                    is ResultState.Error -> {
                        _response.value = UiState.Error(resultState.error)
                    }
                }
            }
        }
    }

}