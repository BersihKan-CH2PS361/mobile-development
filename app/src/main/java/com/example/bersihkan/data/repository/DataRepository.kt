package com.example.bersihkan.data.repository

import android.util.Log
import com.example.bersihkan.BuildConfig
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.local.model.UserModel
import com.example.bersihkan.data.local.pref.UserPreference
import com.example.bersihkan.data.remote.request.EditProfileRequest
import com.example.bersihkan.data.remote.request.LoginRequest
import com.example.bersihkan.data.remote.request.RegisterRequest
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailCollectorResponse
import com.example.bersihkan.data.remote.response.DetailOrderAll
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.remote.response.DetailUserResponse
import com.example.bersihkan.data.remote.response.GeneralResponse
import com.example.bersihkan.data.remote.response.LoginResponse
import com.example.bersihkan.data.remote.response.RegisterResponse
import com.example.bersihkan.data.remote.retrofit.cc.ApiConfig
import com.example.bersihkan.data.remote.retrofit.cc.ApiService
import com.example.bersihkan.data.remote.retrofit.maps.MapsApiConfig
import com.example.bersihkan.helper.isEmailValid
import com.example.bersihkan.utils.UserRole
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

    suspend fun logout(): Flow<ResultState<GeneralResponse>> = flow {
        userPreference.logout()
        try {
            val response = apiService.logout()
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "register: $errorMessage")
        }
    }

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
            Log.d(TAG, "coba sebelum Post")
            val response = apiService.register(
                RegisterRequest(
                    username = username,
                    email = email,
                    name = name,
                    phone = phone,
                    password = password,
                    passwordRepeat = repeatPassword
                )
            )
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
        password: String,
    ): Flow<ResultState<LoginResponse>> = flow {
        try {
            val requestBody = if(isEmailValid(username)) {
                LoginRequest(
                    email = username,
                    password = password
                )
            } else {
                LoginRequest(
                    username = username,
                    password = password
                )
            }
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

    suspend fun updateProfileUser(
        name: String,
        phone: String,
    ): Flow<ResultState<GeneralResponse>> = flow {
        try {
            val user = userPreference.getSession().first()
            val response = apiService.updateUserProfile(
                id = user.id,
                requestBody = EditProfileRequest(
                    name = name,
                    phone = phone,
                    email = user.email
                )
            )
            Log.d(TAG, "coba sesudah Post")
            Log.d(TAG, "register: $response")
            val message = response.status
            Log.d(TAG, "registerMessage: $message")
            when {
                message?.contains("Registered") == true -> {
                    emit(ResultState.Success(response))
                }
                message.isNullOrEmpty() -> {
                    emit(ResultState.Error("Unknown Error"))
                }
                else -> {
                    emit(ResultState.Error(response.status))
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

    suspend fun getDetailHistory(): Flow<ResultState<List<DetailOrderResponse>>> = flow {
        val session = userPreference.getSession().first()
        try {
            val response = apiService.getHistoryUser(session.id)
            val data: List<DetailOrderResponse> = response.map { history ->
                val collector = apiService.getDetailFacilityByOrderId(history.collectorId.toString())
                history.copy(facilityName = collector.first().facilityName)
            }
            Log.d(TAG, "getDetailHistory: resp = $response")
            Log.d(TAG, "getDetailHistory: data = $data")
            emit(ResultState.Success(data))
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

    suspend fun getCurrentOrderUser(): Flow<ResultState<List<DetailOrderResponse>>> = flow {
        val session = userPreference.getSession().first()
        try {
            val response = apiService.getCurrentOrderUser(session.id)
            Log.d(TAG, "getDetailHistory: $response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "getDetailHistory: $errorMessage")
        }
    }

    suspend fun getDetailOrderById(orderId: Int): Flow<ResultState<List<DetailOrderResponse>>> = flow {
        try {
            val response = apiService.getDetailOrderById(orderId)
            Log.d(TAG, "getDetailHistory: $response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "getDetailHistory: $errorMessage")
        }
    }

    suspend fun getDetailUserById(): Flow<ResultState<DetailUserResponse>> = flow {
        val userModel = userPreference.getSession().first()
        try {
            val response = apiService.getDetailUserById(userModel.id)
            Log.d(TAG, "getDetailUserById: $response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "getContents: $errorMessage")
        }
    }

    suspend fun getDetailCollectorById(): Flow<ResultState<DetailCollectorResponse>> = flow {
        val userModel = userPreference.getSession().first()
        try {
            val response = apiService.getDetailFacilityByOrderId(userModel.id)
            Log.d(TAG, "getDetailUserById: $response")
            emit(ResultState.Success(response.first()))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "getContents: $errorMessage")
        }
    }

    suspend fun getAddressFromLatLng(latitude: Float, longitude: Float): String {
        val latlng = "$latitude,$longitude"
        val service = MapsApiConfig.getApiService()

        try {
            val response = service.reverseGeocode(latlng, MAPS_API_KEY)
            if (response.status == "OK" && response.results.isNotEmpty()) {
                return response.results[0].formatted_address
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "Address not found"
    }

    suspend fun getAllDetailOrder(orderId: Int): Flow<ResultState<DetailOrderAll>> = flow {

        try {
            val detailOrder = apiService.getDetailOrderById(orderId).first()

            val detailCollector = apiService.getDetailFacilityByOrderId(detailOrder.collectorId ?: "").first()

            val lat = detailOrder.pickupLatitude ?: 0f
            val lon = detailOrder.pickupLongitude ?: 0f

            val location = getAddressFromLatLng(lat, lon)

            val detailOrderAll = DetailOrderAll(detailOrder, detailCollector, location)

            Log.d(TAG, "detailOrderAll: $detailOrderAll")

            emit(ResultState.Success(detailOrderAll))
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
        private const val MAPS_API_KEY = "AIzaSyCmxT2On0bOukjMCmwXTTN7O3tbtvR8TlI"

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