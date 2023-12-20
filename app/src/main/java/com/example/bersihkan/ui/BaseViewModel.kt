package com.example.bersihkan.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bersihkan.data.local.model.UserModel
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.utils.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BaseViewModel(private val repository: DataRepository) : ViewModel() {

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

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { userModel ->
                _userModel.value = userModel
            }
        }
    }

}