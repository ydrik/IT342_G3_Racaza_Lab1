package com.annimemo.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.annimemo.data.model.UserProfile
import com.google.gson.Gson

/**
 * Secure Token Manager
 * FRS Feature 7.2: Store JWT securely
 */
class TokenManager(private val context: Context) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    fun saveToken(token: String) {
        encryptedSharedPreferences.edit().putString("auth_token", token).apply()
    }
    
    fun getToken(): String? {
        return encryptedSharedPreferences.getString("auth_token", null)
    }
    
    fun saveUser(userJson: String) {
        encryptedSharedPreferences.edit().putString("user_profile", userJson).apply()
    }
    
    fun getUser(): UserProfile? {
        val userJson = encryptedSharedPreferences.getString("user_profile", null)
        return if (userJson != null) {
            try {
                Gson().fromJson(userJson, UserProfile::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
    
    fun getUserRole(): String {
        return getUser()?.role ?: "USER"
    }
    
    fun isAdmin(): Boolean {
        return getUserRole() == "ADMIN"
    }
    
    fun clearAll() {
        encryptedSharedPreferences.edit().clear().apply()
    }
    
    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }
}
