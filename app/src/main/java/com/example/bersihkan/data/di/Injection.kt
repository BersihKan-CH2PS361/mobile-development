package com.example.bersihkan.data.di

import android.content.Context
import android.util.Log
import com.example.bersihkan.data.local.pref.UserPreference
import com.example.bersihkan.data.local.pref.dataStore
import com.example.bersihkan.data.remote.retrofit.ApiConfig
import com.example.bersihkan.data.repository.DataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): DataRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val session = runBlocking { pref.getSession().first() }
        val token = session.token
        Log.d("Injection", "session: $session")
        Log.d("Injection", "session token: $token")
        val apiService = ApiConfig.getApiService(token)
        return DataRepository.getInstance(pref, apiService)
    }

}