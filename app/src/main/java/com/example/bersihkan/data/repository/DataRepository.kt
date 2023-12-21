package com.example.bersihkan.data.repository

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.example.bersihkan.BuildConfig
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.local.model.UserModel
import com.example.bersihkan.data.local.pref.UserPreference
import com.example.bersihkan.data.local.room.PostalCodeDao
import com.example.bersihkan.data.remote.request.EditProfileRequest
import com.example.bersihkan.data.remote.request.LoginRequest
import com.example.bersihkan.data.remote.request.OrderRequest
import com.example.bersihkan.data.remote.request.RegisterRequest
import com.example.bersihkan.data.remote.request.UpdateStatusRequest
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailCollectorByIdResponse
import com.example.bersihkan.data.remote.response.DetailCollectorResponse
import com.example.bersihkan.data.remote.response.DetailOrderAll
import com.example.bersihkan.data.remote.response.DetailOrderCollectorAll
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.remote.response.DetailUserResponse
import com.example.bersihkan.data.remote.response.GeneralResponse
import com.example.bersihkan.data.remote.response.LoginResponse
import com.example.bersihkan.data.remote.response.RegisterResponse
import com.example.bersihkan.data.remote.retrofit.cc.ApiConfig
import com.example.bersihkan.data.remote.retrofit.cc.ApiService
import com.example.bersihkan.data.remote.retrofit.maps.MapsApiConfig
import com.example.bersihkan.helper.isEmailValid
import com.example.bersihkan.ml.RecommendationModel
import com.example.bersihkan.utils.OrderStatus
import com.example.bersihkan.utils.UserRole
import com.example.bersihkan.utils.WasteType
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.HttpException
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Locale
import kotlin.random.Random


@Suppress("DEPRECATION")
class DataRepository private constructor(
    private val context: Context,
    private val userPreference: UserPreference,
    private val dao: PostalCodeDao,
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
            val requestBody = if (isEmailValid(username)) {
                LoginRequest(
                    email = username, password = password
                )
            } else {
                LoginRequest(
                    username = username, password = password
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
                id = user.id, requestBody = EditProfileRequest(
                    name = name, phone = phone, email = user.email
                )
            )
            userPreference.saveSession(
                UserModel(
                    id = user.id,
                    email = user.email,
                    username = user.username,
                    role = user.role,
                    password = user.password,
                    token = user.token,
                    isLogin = true,
                    name = name

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

    suspend fun updateProfileCollector(
        name: String,
        phone: String,
    ): Flow<ResultState<GeneralResponse>> = flow {
        try {
            val user = userPreference.getSession().first()
            val response = apiService.updateCollectorProfile(
                id = user.id, requestBody = EditProfileRequest(
                    name = name, phone = phone, email = user.email
                )
            )
            userPreference.saveSession(
                UserModel(
                    id = user.id,
                    email = user.email,
                    username = user.username,
                    role = user.role,
                    password = user.password,
                    token = user.token,
                    isLogin = true,
                    name = name

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
                val location = getAddressFromLatLng(
                    history.pickupLatitude ?: 0f,
                    history.pickupLongitude ?: 0f
                )
                val collector =
                    apiService.getDetailFacilityByCollectorId(history.collectorId.toString())
                history.copy(
                    facilityName = collector.first().facilityName,
                    pickupLocation = location
                )
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

    suspend fun getDetailHistoryCollector(): Flow<ResultState<List<DetailOrderResponse>>> = flow {
        val session = userPreference.getSession().first()
        try {
            val response = apiService.getHistoryCollector(session.id)
            val data: List<DetailOrderResponse> = response.map { history ->
                val location = getAddressFromLatLng(
                    history.pickupLatitude ?: 0f,
                    history.pickupLongitude ?: 0f
                )
                val collector =
                    apiService.getDetailFacilityByCollectorId(history.collectorId.toString())
                history.copy(
                    facilityName = collector.first().facilityName,
                    pickupLocation = location
                )
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

    suspend fun getCurrentOrderCollector(): Flow<ResultState<List<DetailOrderResponse>>> = flow {
        val session = userPreference.getSession().first()
        try {
            val response = apiService.getCurrentOrderCollector(session.id)
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

    suspend fun getDetailOrderById(orderId: Int): Flow<ResultState<List<DetailOrderResponse>>> =
        flow {
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

    suspend fun createOrder(
        pickupLat: Float,
        pickupLon: Float,
        wasteType: WasteType,
        wasteQty: Int,
        userNotes: String,
        recycleFee: Int,
        pickupFee: Int,
        subtotalFee: Int,
    ): Flow<ResultState<GeneralResponse>> = flow {
        try {
            val postalCode = getPostalCodeFromLocation(pickupLat.toDouble(), pickupLon.toDouble())
            Log.d(TAG, "createOrder: postalCode = $postalCode")
            if(postalCode == 0) {
                val errorMsg =
                    "Sorry, BersihKan service is not available yet in your current location :(\n\nWe'll reach out to you as soon as possible."
                emit(ResultState.Error(errorMsg))
            }
            val facilityId = getFacilityIdFromPostalCode(postalCode)
            Log.d(TAG, "createOrder: facilityId = $facilityId")
            if (facilityId != -1) {
                val collectorId =
                    apiService.getDetailFacilityByFacilityId(facilityId.toString())
                val body = OrderRequest(
                    facilityId = collectorId.userId.toString(),
                    pickupFee = pickupFee,
                    pickupLatitude = pickupLat.toLong(),
                    pickupLongitude = pickupLon.toLong(),
                    userNotes = userNotes,
                    subtotalFee = subtotalFee,
                    wasteType = wasteType.type,
                    recycleFee = recycleFee,
                    wasteQty = wasteQty
                )
                Log.d(TAG, "orderRequest: $body")
                Log.d(TAG, "subtotalFee: $subtotalFee")
                val user = userPreference.getSession().first()
                val response = apiService.createOrder(user.id, requestBody = body)
                Log.d(TAG, "createOrder: $response")
                emit(ResultState.Success(response))
            } else{
                val errorMsg = "Sorry, BersihKan service is not available yet in your current location :(\n\nWe'll reach out to you as soon as possible."
                emit(ResultState.Error(errorMsg))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, GeneralResponse::class.java)
            val errorMessage = errorBody.status
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "createOrder: $errorMessage")
        }
    }

    suspend fun updateOrderStatus(
        orderId: Int,
        orderStatus: OrderStatus
    ): Flow<ResultState<GeneralResponse>> = flow {
        try {
            val body = UpdateStatusRequest(orderStatus.status)
            val response = apiService.updateOrderStatus(orderId, body)
            Log.d(TAG, "updateOrderStatus: $response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, GeneralResponse::class.java)
            val errorMessage = errorBody.status
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "getContents: $errorMessage")
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

    suspend fun getDetailCollectorById(): Flow<ResultState<DetailCollectorByIdResponse>> = flow {
        val userModel = userPreference.getSession().first()
        try {
            val response = apiService.getDetailFacilityByCollectorId(userModel.id)
            Log.d(TAG, "getDetailUserById: $response")
            val data = response.first()
            emit(ResultState.Success(data))
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

    private fun getPostalCodeFromLocation(latitude: Double, longitude: Double): Int {
        val geocoder = Geocoder(context, Locale.getDefault())
        var postalCode = ""

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true && addresses.get(0)?.postalCode != null) {
                postalCode = addresses[0].postalCode ?: ""
                Log.d(TAG, "postalCode: $postalCode, Latlng: $latitude, $longitude")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return postalCode.toIntOrNull() ?: 0
    }

    suspend fun getAllDetailOrder(orderId: Int): Flow<ResultState<DetailOrderAll>> = flow {

        try {
            val detailOrder = apiService.getDetailOrderById(orderId).first()

            val detailCollector =
                apiService.getDetailFacilityByCollectorId(detailOrder.collectorId ?: "").first()

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

    suspend fun getAllDetailOrderCollector(orderId: Int): Flow<ResultState<DetailOrderCollectorAll>> =
        flow {

            try {
                val detailOrder = apiService.getDetailOrderById(orderId).first()

                val lat = detailOrder.pickupLatitude ?: 0f
                val lon = detailOrder.pickupLongitude ?: 0f

                val location = getAddressFromLatLng(lat, lon)

                val detailOrderAll = DetailOrderCollectorAll(detailOrder, location)

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

    private suspend fun getFacilityIdFromPostalCode(postalCode: Int): Int {
        val idx = getPostalCodeIdx(postalCode = postalCode)
        if(idx != -1){
            val recommendedIds = runMLModel(postalCode)
            if (recommendedIds.isEmpty()) {
                return -1
            }
            return selectFacilityId(recommendedIds.toMutableList())
        }
        return -1
    }

    private suspend fun getPostalCodeIdx(postalCode: Int): Int {
        return withContext(Dispatchers.IO) {
            try {
                val postalCodeData = dao.getIdxByPostalCode(postalCode)
                Log.d(TAG, "postalCodeData: $postalCodeData")
                if (postalCodeData != null) {
                    val idx = postalCodeData.postalCodeIdx
                    Log.d(TAG, "Postal Code Index: $idx")
                    return@withContext idx
                } else {
                    Log.d(TAG, "No postal index found for postal code: $postalCode")
                    return@withContext -1
                }
            } catch (e: Exception) {
                Log.d(TAG, "getPostalCodeIdx: ${e.message}")
                return@withContext -1
            }
        }
    }

    private suspend fun selectFacilityId(recommendedIds: MutableList<Int>): Int {
        if (recommendedIds.isEmpty()) {
            // If the list of recommendedIds is empty, return a default value or handle as needed
            Log.d(TAG, "selectFacilityId: No recommendation found")
            return -1
        }

        val randomIndex = Random.nextInt(recommendedIds.size)
        val newFacilityId = recommendedIds.removeAt(randomIndex) // Remove and obtain the random ID
        try {
            val collectorId = apiService.getDetailFacilityByFacilityId(newFacilityId.toString())
            if (collectorId.facilityId != null) {
                return newFacilityId // Return if collectorId exists for the chosen facilityId
            }
        } catch (e: Exception) {
            // Handle exception if necessary, currently not doing anything specific
        }

        // Recursive call to select another ID from the modified list (without the chosen ID)
        return selectFacilityId(recommendedIds)
    }

    private fun runMLModel(postalCodeIdx: Int): List<Int> {
        val model = RecommendationModel.newInstance(context)

        val postalCodeValue = postalCodeIdx.toFloat()

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature0.loadBuffer(ByteBuffer.allocate(4).putFloat(postalCodeValue))

        val inputFeature1 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(ByteBuffer.allocate(4).putFloat(0f))

        val inputFeature2 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(ByteBuffer.allocate(4).putFloat(1f))

        val inputFeature3 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(ByteBuffer.allocate(4).putFloat(1f))

        val inputFeature4 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(ByteBuffer.allocate(4).putFloat(1f))

        val inputFeature5 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(ByteBuffer.allocate(4).putFloat(1f))

        val inputFeature6 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(ByteBuffer.allocate(4).putFloat(1f))

        val inputFeature7 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(ByteBuffer.allocate(4).putFloat(1f))

        val inputFeature8 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(ByteBuffer.allocate(4).putFloat(1f))

        val inputFeature9 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature9.loadBuffer(ByteBuffer.allocate(4).putFloat(1f))

        // Run model inference with the prepared input features
        val outputs = model.process(inputFeature0, inputFeature1, inputFeature2, inputFeature3, inputFeature4, inputFeature5, inputFeature6, inputFeature7, inputFeature8, inputFeature9)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        model.close()

        Log.d(TAG, "runMLModel: ${outputFeature0.floatArray[0]}")

        val topK = 10 // Number of top recommendations

        val indicesWithValues = outputFeature0.floatArray.withIndex().toList()

        // Sort indices based on their corresponding values
        val sortedIndices = indicesWithValues.sortedByDescending { it.value }.take(topK)

        // Extract only indices from the sorted list
        val recommendedIds = sortedIndices.map { it.index }
        Log.d(TAG, "processedMLOutput: $recommendedIds")

        return recommendedIds
    }

    companion object {
        private const val TAG = "DataRepository"
        private const val MAPS_API_KEY = BuildConfig.MAPS_TOKEN

        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            dao: PostalCodeDao,
            apiService: ApiService, context: Context,
        ): DataRepository = instance ?: synchronized(this) {
            instance ?: DataRepository(context, userPreference, dao, apiService)
        }.also { instance = it }
    }

}