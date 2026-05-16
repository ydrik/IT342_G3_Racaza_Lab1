package com.annimemo.network

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Auth Interceptor
 * FRS Feature 7.2: JWT Authentication
 * Adds JWT token to Authorization header for all API requests
 */
class AuthInterceptor(private val context: Context) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Get JWT token from secured SharedPreferences
        val token = getTokenFromSecureStorage(context)
        
        return if (token.isNotEmpty()) {
            // Add Authorization header with Bearer token
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
    
    private fun getTokenFromSecureStorage(context: Context): String {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "auth_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            
            sharedPreferences.getString("auth_token", "") ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}
