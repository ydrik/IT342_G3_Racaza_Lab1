package com.annimemo.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.annimemo.R
import com.annimemo.data.TokenManager

/**
 * User Profile Activity
 * FRS Feature 1: User profile view
 */
class UserProfileActivity : AppCompatActivity() {
    
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var roleTextView: TextView
    private lateinit var backButton: Button
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        
        tokenManager = TokenManager(this)
        
        initializeViews()
        displayUserInfo()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        usernameTextView = findViewById(R.id.usernameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        roleTextView = findViewById(R.id.roleTextView)
        backButton = findViewById(R.id.buttonBack)
    }
    
    private fun displayUserInfo() {
        val user = tokenManager.getUser()
        if (user != null) {
            usernameTextView.text = user.username
            emailTextView.text = user.email
            roleTextView.text = user.role
        }
    }
    
    private fun setupClickListeners() {
        backButton.setOnClickListener { finish() }
    }
}
