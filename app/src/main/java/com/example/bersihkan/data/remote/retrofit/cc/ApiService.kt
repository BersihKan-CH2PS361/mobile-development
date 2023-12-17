package com.example.bersihkan.data.remote.retrofit.cc

import com.example.bersihkan.data.remote.request.EditProfileRequest
import com.example.bersihkan.data.remote.request.LoginRequest
import com.example.bersihkan.data.remote.request.OrderRequest
import com.example.bersihkan.data.remote.request.RegisterRequest
import com.example.bersihkan.data.remote.request.UpdateStatusRequest
import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailCollectorResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.remote.response.DetailUserResponse
import com.example.bersihkan.data.remote.response.GeneralResponse
import com.example.bersihkan.data.remote.response.LoginResponse
import com.example.bersihkan.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("/auth/register-user")
    suspend fun register(
        @Body requestBody: RegisterRequest
    ): RegisterResponse

    @POST("/auth/login")
    suspend fun login(
        @Body requestBody: LoginRequest
    ): LoginResponse

    @POST("/auth/logout")
    suspend fun logout(): GeneralResponse

    @GET("/users/{id}")
    suspend fun getDetailUserById(
        @Path("id") id: String
    ): DetailUserResponse

    @GET("/collectors/{id}")
    suspend fun getDetailCollector(
        @Path("id") id: String
    ): DetailUserResponse

    @PUT("/users/update-profile/{id}")
    suspend fun updateUserProfile(
        @Path("id") id: String,
        @Body requestBody: EditProfileRequest
    ): GeneralResponse

    @PUT("/collectors/update-profile/{id}")
    suspend fun updateCollectorProfile(
        @Path("id") id: String,
        @Body requestBody: EditProfileRequest
    ): GeneralResponse

    @POST("/orders/create/{id}")
    suspend fun createOrder(
        @Path("id") id: String,
        @Body requestBody: OrderRequest
    ): GeneralResponse

    @GET("/orders/{id}")
    suspend fun getDetailOrderById(
        @Path("id") id: Int
    ): List<DetailOrderResponse>

    @PUT(" /orders/update-status/{id}")
    suspend fun updateOrderStatus(
        @Path("id") id: String,
        @Body requestBody: UpdateStatusRequest
    )

    @GET("/orders/history-user/{id}")
    suspend fun getHistoryUser(
        @Path("id") id: String
    ): List<DetailOrderResponse>

    @GET("/orders/history-collector/{id}")
    suspend fun getHistoryCollector(
        @Path("id") id: String
    ): List<DetailOrderResponse>

    @GET("/orders/orderdata-collector/{id}")
    suspend fun getCurrentOrderCollector(
        @Path("id") id: String
    ): List<DetailOrderResponse>

    @GET("/orders/orderdata-user/{id}")
    suspend fun getCurrentOrderUser(
        @Path("id") id: String
    ): List<DetailOrderResponse>

    @GET("/contents")
    suspend fun getContents(): List<ContentsResponse>

    @GET("/facility/search-user/{id}")
    suspend fun getDetailFacilityByOrderId(
        @Path("id") id: String
    ): List<DetailCollectorResponse>

}