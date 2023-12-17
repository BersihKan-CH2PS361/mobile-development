package com.example.bersihkan.data.remote.retrofit.maps

import com.example.bersihkan.data.remote.response.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApiService {

    @GET("maps/api/geocode/json")
    suspend fun reverseGeocode(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): GeocodingResponse

}