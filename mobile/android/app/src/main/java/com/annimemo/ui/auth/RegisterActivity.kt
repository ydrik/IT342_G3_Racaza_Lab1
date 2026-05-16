package com.annimemo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.annimemo.R
import com.annimemo.data.TokenManager
import com.annimemo.data.model.RegisterRequest
import com.annimemo.network.RetrofitClient
import com.annimemo.ui.pet.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 * Register Activity
 * FRS Feature 1: User Registration
 */
class RegisterActivity : AppCompatActivity() {

    companion object {
        private val PASSWORD_POLICY_REGEX = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9])\\S+$")
    }
    
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        tokenManager = TokenManager(this)
        
        initializeViews()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        usernameEditText = findViewById(R.id.editTextUsername)
        emailEditText = findViewById(R.id.editTextEmail)
        firstNameEditText = findViewById(R.id.editTextFirstName)
        lastNameEditText = findViewById(R.id.editTextLastName)
        passwordEditText = findViewById(R.id.editTextPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        registerButton = findViewById(R.id.buttonRegister)
        loginButton = findViewById(R.id.buttonLogin)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun setupClickListeners() {
        registerButton.setOnClickListener { handleRegister() }
        loginButton.setOnClickListener { navigateToLogin() }
    }
    
    private fun handleRegister() {
        val username = usernameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        
        if (!validateInput(username, email, firstName, lastName, password, confirmPassword)) {
            return
        }
        
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val registerRequest = RegisterRequest(username, password, firstName, lastName, email)
                val response = RetrofitClient.getApiService().register(registerRequest)
                
                // Save JWT token
                tokenManager.saveToken(response.token)
                
                // Save user profile
                val userJson = Gson().toJson(response.user)
                tokenManager.saveUser(userJson)
                
                Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
                
            } catch (e: Exception) {
                Log.e("RegisterActivity", "Registration failed", e)
                Toast.makeText(
                    this@RegisterActivity,
                    "Registration failed. Please review your input and try again.",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun validateInput(
        username: String, email: String, firstName: String,
        lastName: String, password: String, confirmPassword: String
    ): Boolean {
        when {
            username.isEmpty() -> {
                usernameEditText.error = "Username required"
                return false
            }
            username.length < 3 -> {
                usernameEditText.error = "Username must be between 3 and 30 characters"
                return false
            }
            username.length > 30 -> {
                usernameEditText.error = "Username must be between 3 and 30 characters"
                return false
            }
            email.isEmpty() -> {
                emailEditText.error = "Email required"
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailEditText.error = "Invalid email"
                return false
            }
            email.length > 120 -> {
                emailEditText.error = "Email must be at most 120 characters"
                return false
            }
            firstName.isEmpty() -> {
                firstNameEditText.error = "First name required"
                return false
            }
            firstName.length > 50 -> {
                firstNameEditText.error = "First name must be at most 50 characters"
                return false
            }
            lastName.isEmpty() -> {
                lastNameEditText.error = "Last name required"
                return false
            }
            lastName.length > 50 -> {
                lastNameEditText.error = "Last name must be at most 50 characters"
                return false
            }
            password.isEmpty() -> {
                passwordEditText.error = "Password required"
                return false
            }
            password.length < 12 || password.length > 100 -> {
                passwordEditText.error = "Password must be between 12 and 100 characters"
                return false
            }
            !PASSWORD_POLICY_REGEX.matches(password) -> {
                passwordEditText.error = "Password must include uppercase, lowercase, number, special character, and no spaces"
                return false
            }
            password != confirmPassword -> {
                confirmPasswordEditText.error = "Passwords don't match"
                return false
            }
        }
        return true
    }
    
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
