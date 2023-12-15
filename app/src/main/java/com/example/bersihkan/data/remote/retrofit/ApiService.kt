package com.example.bersihkan.data.remote.retrofit

import com.example.bersihkan.data.remote.response.ContentsResponse
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
        @Body requestBody: Map<String, String>
    ): RegisterResponse

    @POST("/auth/login")
    suspend fun login(
        @Body requestBody: Map<String, String>
    ): LoginResponse

    @POST("/auth/logout")
    suspend fun logout(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): GeneralResponse

    @GET("/users/{id}")
    suspend fun getDetailUser(
        @Path("id") id: String
    ): DetailUserResponse

    @GET("/collectors/{id}")
    suspend fun getDetailCollector(
        @Path("id") id: String
    ): DetailUserResponse

    @PUT("/users/update-profile/{id}")
    suspend fun updateUserProfile(
        @Path("id") id: String,
        @Query("name") name: String,
        @Query("phone") phone: String,
    ): GeneralResponse

    @POST("/orders/create/{id}")
    suspend fun createOrder(
        @Path("id") id: String,
        @Query("waste_type") wasteType: String,
        @Query("waste_qty") wasteQty: Long,
        @Query("user_notes") userNotes: String,
        @Query("recycle_fee") recycleFee: Long,
        @Query("pickup_latitude") pickupLatitude: Float,
        @Query("pickup_longitude") pickupLongitude: Float
    ): GeneralResponse

    @GET("/orders/{id}")
    suspend fun getDetailOrder(
        @Path("id") id: String
    ): DetailOrderResponse

    @PUT(" /orders/update-status/{id}")
    suspend fun updateOrderStatus(
        @Path("id") id: String,
        @Query("order_status") orderStatus: String
    )

    @GET("/orders/history-user/{id}")
    suspend fun getHistoryUser(
        @Path("id") id: String
    ): DetailOrderResponse

    @GET("/orders/history-collector/{id}")
    suspend fun getHistoryCollector(
        @Path("id") id: String
    ): DetailOrderResponse

    @GET("/orders/orderdata-collector/{id}")
    suspend fun getCurrentOrderCollector(
        @Path("id") id: String
    ): DetailOrderResponse

    @GET("/orders/orderdata-user/{id}")
    suspend fun getCurrentOrderUser(
        @Path("id") id: String
    ): DetailOrderResponse

    @GET("/contents")
    suspend fun getContents(): List<ContentsResponse>

}