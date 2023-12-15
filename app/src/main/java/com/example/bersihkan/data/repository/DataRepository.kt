package com.example.bersihkan.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.local.model.UserModel
import com.example.bersihkan.data.local.pref.UserPreference
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponseItem
import com.example.bersihkan.data.remote.response.GeneralResponse
import com.example.bersihkan.data.remote.response.LoginResponse
import com.example.bersihkan.data.remote.response.RegisterResponse
import com.example.bersihkan.data.remote.retrofit.ApiConfig
import com.example.bersihkan.data.remote.retrofit.ApiService
import com.example.bersihkan.utils.UserRole
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class DataRepository private constructor(
    private val userPreference: UserPreference,
    private var apiService: ApiService
) {

    private fun updateApiService(newToken: String) {
        apiService = ApiConfig.getApiService(newToken)
    }

    private suspend fun saveSession(userModel: UserModel) {
        userPreference.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> = userPreference.getSession()

    suspend fun logout() = userPreference.logout()

    suspend fun register(
        username: String,
        name: String,
        email: String,
        password: String,
        repeatPassword: String,
        phone: String,
    ): Flow<ResultState<RegisterResponse>> = flow {
        Log.d(TAG, "coba Log")
        try {
            Log.d(TAG, "coba Try")
            val requestBody = mapOf(
                "username" to username,
                "email" to email,
                "password" to password,
                "password_repeat" to repeatPassword,
                "phone" to phone,
                "name" to name
            )
            Log.d(TAG, "coba sebelum Post")
            val response = apiService.register(requestBody)
            Log.d(TAG, "coba sesudah Post")
            Log.d(TAG, "register: $response")
            val message = response.message
            Log.d(TAG, "registerMessage: $message")
            when {
                message?.contains("Registered") == true -> {
                    emit(ResultState.Success(response))
                }
                message.isNullOrEmpty() -> {
                    emit(ResultState.Error("Unknown Error"))
                }
                else -> {
                    emit(ResultState.Error(response.message))
                }
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "register: $errorMessage")
        }
    }


    suspend fun login(
        username: String,
        email: String,
        password: String,
    ): Flow<ResultState<LoginResponse>> = flow {
        try {
            val requestBody = mapOf(
                "username" to username,
                "email" to email,
                "password" to password,
            )
            val response = apiService.login(requestBody)
            Log.d(TAG, "login: $response")
            val data = response.user
            val token = response.token
            val role = if (data?.role == UserRole.USER.role) UserRole.USER else UserRole.COLLECTOR
            if (token != null && data?.id != null && data.username != null && data.email != null && data.name != null) {
                val userModel = UserModel(
                    id = data.id,
                    email = data.email,
                    username = data.username,
                    role = role,
                    password = password,
                    token = token,
                    isLogin = true,
                    name = data.name
                )
                saveSession(userModel)
                Log.d(TAG, "userModel: $userModel")
                updateApiService(newToken = token)
                Log.d(TAG, "userPref: ${userPreference.getSession().first()}")
                emit(ResultState.Success(response))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "login: $errorMessage")
        }
    }

    suspend fun getDetailHistory(): Flow<ResultState<List<DetailOrderResponseItem>>> = flow {
        val session = userPreference.getSession().first()
        try {
            val response = apiService.getDetailOrder(session.id)
            val listHistory = response.detailOrderResponse?.filterNotNull()
            Log.d(TAG, "getDetailHistory: $response")
            emit(ResultState.Success(listHistory ?: emptyList()))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "getDetailHistory: $errorMessage")
        }
    }
    suspend fun getContents(): Flow<ResultState<List<ContentsResponse>>> = flow {
        try {
            val response = apiService.getContents()
            Log.d(TAG, "getContents: $response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "getContents: $errorMessage")
        }
    }

    companion object {
        private const val TAG = "DataRepository"

        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(userPreference, apiService)
            }.also { instance = it }
    }

}