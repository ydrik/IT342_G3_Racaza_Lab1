package com.annimemo.ui.admin

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annimemo.R
import com.annimemo.data.TokenManager
import com.annimemo.network.RetrofitClient
import kotlinx.coroutines.launch

/**
 * Admin Panel Activity
 * FRS Feature 2: Role-Based Access Control
 * Admin-only UI for user management
 */
class AdminPanelActivity : AppCompatActivity() {
    
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var backButton: Button
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
        
        tokenManager = TokenManager(this)
        
        // Check if user is admin
        if (!tokenManager.isAdmin()) {
            Toast.makeText(this, "Access denied. Admin privileges required.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initializeViews()
        setupRecyclerView()
        setupClickListeners()
        loadUsers()
    }
    
    private fun initializeViews() {
        userRecyclerView = findViewById(R.id.userRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        backButton = findViewById(R.id.buttonBack)
    }
    
    private fun setupRecyclerView() {
        userRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    
    private fun setupClickListeners() {
        backButton.setOnClickListener { finish() }
    }
    
    private fun loadUsers() {
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val users = RetrofitClient.getApiService().getAllUsers()
                // Set up adapter
                Toast.makeText(
                    this@AdminPanelActivity,
                    "Loaded ${users.size} users",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@AdminPanelActivity,
                    "Failed to load users: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
}
