package com.example.bersihkan.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bersihkan.data.repository.DataRepository
import com.example.bersihkan.ui.MainViewModel
import com.example.bersihkan.ui.screen.collector.delivery.DeliveryCollectorViewModel
import com.example.bersihkan.ui.screen.collector.detailHistory.DetailCollectorViewModel
import com.example.bersihkan.ui.screen.collector.editProfile.EditProfileCollectorViewModel
import com.example.bersihkan.ui.screen.collector.history.HistoryCollectorViewModel
import com.example.bersihkan.ui.screen.collector.home.HomeCollectorViewModel
import com.example.bersihkan.ui.screen.collector.profile.ProfileCollectorViewModel
import com.example.bersihkan.ui.screen.customer.delivery.DeliveryViewModel
import com.example.bersihkan.ui.screen.customer.detailHistory.DetailViewModel
import com.example.bersihkan.ui.screen.customer.editProfile.EditProfileViewModel
import com.example.bersihkan.ui.screen.customer.history.HistoryViewModel
import com.example.bersihkan.ui.screen.customer.home.HomeViewModel
import com.example.bersihkan.ui.screen.customer.order.OrderViewModel
import com.example.bersihkan.ui.screen.customer.profile.ProfileViewModel
import com.example.bersihkan.ui.screen.customer.statistics.StatisticsViewModel
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
        if(modelClass.isAssignableFrom(StatisticsViewModel::class.java)){
            return StatisticsViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(DeliveryViewModel::class.java)){
            return DeliveryViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(OrderViewModel::class.java)){
            return OrderViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(HomeCollectorViewModel::class.java)){
            return HomeCollectorViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(HistoryCollectorViewModel::class.java)){
            return HistoryCollectorViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(DetailCollectorViewModel::class.java)){
            return DetailCollectorViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(ProfileCollectorViewModel::class.java)){
            return ProfileCollectorViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(EditProfileCollectorViewModel::class.java)){
            return EditProfileCollectorViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(DeliveryCollectorViewModel::class.java)){
            return DeliveryCollectorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}