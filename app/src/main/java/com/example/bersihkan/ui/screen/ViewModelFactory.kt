package com.example.bersihkan.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.ui.screen.customer.detailHistory.DetailViewModel
import com.example.bersihkan.ui.screen.customer.editProfile.EditProfileViewModel
import com.example.bersihkan.ui.screen.customer.history.HistoryViewModel
import com.example.bersihkan.ui.screen.customer.home.HomeViewModel
import com.example.bersihkan.ui.screen.customer.profile.ProfileViewModel
import com.example.bersihkan.ui.screen.general.login.LoginViewModel
import com.example.bersihkan.ui.screen.general.register.RegisterViewModel

class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(EditProfileViewModel::class.java)){
            return EditProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}