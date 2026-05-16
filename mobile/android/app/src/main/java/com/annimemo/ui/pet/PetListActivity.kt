package com.annimemo.ui.pet

import android.content.Intent
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
import com.annimemo.ui.auth.LoginActivity
import kotlinx.coroutines.launch

/**
 * Pet List Activity
 * FRS Feature 3: Core Business Module - Display all pets
 */
class PetListActivity : AppCompatActivity() {
    
    private lateinit var petRecyclerView: RecyclerView
    private lateinit var addPetButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_list)
        
        tokenManager = TokenManager(this)
        
        if (!tokenManager.isLoggedIn()) {
            navigateToLogin()
            return
        }
        
        initializeViews()
        setupRecyclerView()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        petRecyclerView = findViewById(R.id.petRecyclerView)
        addPetButton = findViewById(R.id.buttonAddPet)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun setupRecyclerView() {
        petRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    
    private fun setupClickListeners() {
        addPetButton.setOnClickListener {
            startActivity(Intent(this, AddPetActivity::class.java))
        }
    }
    
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
