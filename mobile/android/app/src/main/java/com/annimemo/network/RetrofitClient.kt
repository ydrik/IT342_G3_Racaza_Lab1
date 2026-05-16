package com.annimemo.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit Client
 * FRS Feature 7.5: API Integration Configuration
 * Singleton instance for API communication
 */
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // Android emulator localhost
    
    private lateinit var apiService: ApiService
    
    fun initialize(context: Context) {
        if (!::apiService.isInitialized) {
            val httpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor(context))
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
            
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            apiService = retrofit.create(ApiService::class.java)
        }
    }
    
    fun getApiService(): ApiService {
        if (!::apiService.isInitialized) {
            throw IllegalStateException("RetrofitClient not initialized. Call initialize() first.")
        }
        return apiService
    }
}
