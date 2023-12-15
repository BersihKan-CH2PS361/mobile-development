package com.example.bersihkan.ui.screen.general.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.remote.response.LoginResponse
import com.example.bersihkan.data.remote.response.RegisterResponse
import com.example.bersihkan.data.repository.DataRepository
import com.example.kekkomiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: DataRepository) : ViewModel() {

    private var _response: MutableStateFlow<UiState<LoginResponse>> = MutableStateFlow(UiState.Initial)
    val response: StateFlow<UiState<LoginResponse>> get() = _response

    var inputUsername = mutableStateOf("")
    var inputEmail = mutableStateOf("")
    var errorEmail = mutableStateOf(false)
    var inputPassword = mutableStateOf("")
    var errorPassword = mutableStateOf(false)

    private var _isEnabled: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    val isEnabled get() = _isEnabled

    private fun enableButton() {
        _isEnabled.value = inputEmail.value != "" && inputUsername.value != "" && inputPassword.value != "" && errorEmail.value == false && errorPassword.value == false
    }

    fun login(){
        viewModelScope.launch {
            _response.value = UiState.Loading
            repository.login(
                username = inputUsername.value,
                email = inputEmail.value,
                password = inputPassword.value,
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

    fun setUsername(newInput: String){
        inputUsername.value = newInput
        enableButton()
    }

    fun setEmail(newInput: String){
        inputEmail.value = newInput
        enableButton()
    }


    fun setPassword(newInput: String){
        inputPassword.value = newInput
        enableButton()
    }

    fun setErrorEmail(newInput: Boolean){
        errorEmail.value = newInput
        enableButton()
    }

    fun setErrorPassword(newInput: Boolean){
        errorPassword.value = newInput
        enableButton()
    }

}