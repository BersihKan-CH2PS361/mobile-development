package com.example.bersihkan.data.repository

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.example.bersihkan.BuildConfig
import com.example.bersihkan.data.ResultState
import com.example.bersihkan.data.local.model.UserModel
import com.example.bersihkan.data.local.pref.UserPreference
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
import com.example.bersihkan.ml.TFLiteModel
import com.example.bersihkan.utils.OrderStatus
import com.example.bersihkan.utils.UserRole
import com.example.bersihkan.utils.WasteType
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.HttpException
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Locale

@Suppress("DEPRECATION")
class DataRepository private constructor(
    private val context: Context,
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
                val location = getAddressFromLatLng(history.pickupLatitude ?: 0f, history.pickupLongitude ?: 0f)
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
                val location = getAddressFromLatLng(history.pickupLatitude ?: 0f, history.pickupLongitude ?: 0f)
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
            val facilityId = 5f
            Log.d(TAG, "createOrder: facilityId = ${facilityId.toInt()}")
            val collectorId = apiService.getDetailFacilityByFacilityId(facilityId.toInt().toString())
            if(facilityId != 0f ){
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
                val user = userPreference.getSession().first()
                val response = apiService.createOrder(user.id, requestBody = body)
                Log.d(TAG, "createOrder: $response")
                emit(ResultState.Success(response))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, GeneralResponse::class.java)
            val errorMessage = errorBody.status
            errorMessage?.let { emit(ResultState.Error(it)) }
            Log.e(TAG, "getContents: $errorMessage")
        }
    }

    suspend fun updateOrderStatus(orderId: Int, orderStatus: OrderStatus): Flow<ResultState<GeneralResponse>> = flow{
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

    private fun getPostalCodeFromLocation(latitude: Double, longitude: Double): Int {
        val geocoder = Geocoder(context, Locale.getDefault())
        var postalCode = ""

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                postalCode = addresses[0].postalCode ?: ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return postalCode.toInt()
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

    suspend fun getAllDetailOrderCollector(orderId: Int): Flow<ResultState<DetailOrderCollectorAll>> = flow {

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

    private fun getFacilityIdFromMLModel(postalCode: Int): FloatArray {
        val tfliteModel = TFLiteModel(context)
        tfliteModel.loadModel()
        val inputData = floatArrayOf(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, postalCode.toFloat())

        // Prepare input data (postal code) for the model
        val byteBuffer = ByteBuffer.allocateDirect(inputData.size * Float.SIZE_BYTES)
        byteBuffer.order(ByteOrder.nativeOrder())
        for (value in inputData) {
            byteBuffer.putFloat(value)
        }
        byteBuffer.rewind()

        // Perform inference and return the result
        return tfliteModel.doInference(inputData)
    }

    private fun runMLModel(postalCode: Int): Float {
        val model = RecommendationModel.newInstance(context)

        // Assuming your model expects 10 input features and the postal code is one of them
        val numberOfFeatures = 10

        // Create TensorBuffer instances for each input feature
        val inputBuffers = Array(numberOfFeatures) {
            TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        }

        // Assuming you have an array of input data with 10 values including postal code
        val inputData = floatArrayOf(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, postalCode.toFloat())

        // Load input data into the corresponding TensorBuffer instances
        for (i in 0 until numberOfFeatures) {
            val byteBuffer = ByteBuffer.allocateDirect(Float.SIZE_BYTES)
            byteBuffer.order(ByteOrder.nativeOrder())
            byteBuffer.putFloat(inputData[i])
            byteBuffer.rewind()

            // Load data into the TensorBuffer
            inputBuffers[i].loadBuffer(byteBuffer)
        }

        // Run model inference with the prepared input features
        val outputs = model.process(inputBuffers[0], inputBuffers[1], inputBuffers[2], inputBuffers[3],
            inputBuffers[4], inputBuffers[5], inputBuffers[6], inputBuffers[7],
            inputBuffers[8], inputBuffers[9])

        // Get the output TensorBuffer
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

// Releases model resources if no longer used.
        model.close()

        Log.d(TAG, "outputML: ${outputFeature0.buffer}")
        Log.d(TAG, "outputML: ${outputFeature0.floatArray[0]}")
        Log.d(TAG, "outputML: ${outputFeature0.intArray[0]}")
        Log.d(TAG, "outputML: ${outputFeature0.dataType}")
        Log.d(TAG, "outputML: ${outputFeature0.shape}")
        Log.d(TAG, "outputML: ${outputFeature0.flatSize}")
        Log.d(TAG, "outputML: ${outputFeature0.isDynamic}")
        Log.d(TAG, "outputML: ${outputFeature0.typeSize}")

        return outputFeature0.floatArray[0]
    }

    private fun createByteBufferForInt(value: Int): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(Int.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
        byteBuffer.putInt(value)
        byteBuffer.rewind()
        return byteBuffer
    }

    companion object {
        private const val TAG = "DataRepository"
        private const val MAPS_API_KEY = BuildConfig.MAPS_TOKEN

        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService, context: Context
        ): DataRepository = instance ?: synchronized(this) {
            instance ?: DataRepository(context, userPreference, apiService)
        }.also { instance = it }
    }

}