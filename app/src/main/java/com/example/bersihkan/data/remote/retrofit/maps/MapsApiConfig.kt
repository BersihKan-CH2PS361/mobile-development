package com.example.bersihkan.data.remote.retrofit.maps

import android.util.Log
import com.example.bersihkan.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsApiConfig() {

    companion object {
        private const val MAPS_URL = BuildConfig.MAPS_URL
        private const val MAPS_TOKEN = BuildConfig.MAPS_TOKEN
        fun getApiService(): MapsApiService {
            val loggingInterceptor =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            Log.d("ApiConfig", "BASE_URL $MAPS_URL")
            val retrofit = Retrofit.Builder()
                .baseUrl(MAPS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(MapsApiService::class.java)
        }
    }

}