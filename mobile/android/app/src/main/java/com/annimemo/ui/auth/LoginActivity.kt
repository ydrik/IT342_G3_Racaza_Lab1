package com.annimemo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.annimemo.R
import com.annimemo.data.TokenManager
import com.annimemo.data.model.LoginRequest
import com.annimemo.network.RetrofitClient
import com.annimemo.ui.pet.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 * Login Activity
 * FRS Feature 1: User Login
 * FRS Feature 7.2: JWT Authentication
 */
class LoginActivity : AppCompatActivity() {
    
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        tokenManager = TokenManager(this)
        
        // Check if already logged in
        if (tokenManager.isLoggedIn()) {
            navigateToMainActivity()
            return
        }
        
        initializeViews()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        registerButton = findViewById(R.id.buttonRegister)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun setupClickListeners() {
        loginButton.setOnClickListener { handleLogin() }
        registerButton.setOnClickListener { navigateToRegister() }
    }
    
    private fun handleLogin() {
        val identifier = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString()
        
            if (!validateInput(identifier, password)) {
            return
        }
        
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(identifier, password)
                val response = RetrofitClient.getApiService().login(loginRequest)
                
                // Save JWT token securely
                tokenManager.saveToken(response.token)
                
                // Save user profile
                val userJson = Gson().toJson(response.user)
                tokenManager.saveUser(userJson)
                
                Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
                
            } catch (e: Exception) {
                Log.e("LoginActivity", "Login failed", e)
                Toast.makeText(
                    this@LoginActivity,
                    "Invalid credentials",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun validateInput(identifier: String, password: String): Boolean {
        when {
            identifier.isEmpty() -> {
                usernameEditText.error = "Email or username required"
                return false
            }
            password.isEmpty() -> {
                passwordEditText.error = "Password required"
                return false
            }
            password.length < 6 -> {
                passwordEditText.error = "Password must be at least 8 characters"
                return false
            }
        }
        return true
    }
    
    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
    
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
