package com.annimemo.ui.pet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annimemo.R
import com.annimemo.data.TokenManager
import com.annimemo.data.model.PetResponse
import com.annimemo.network.RetrofitClient
import com.annimemo.ui.admin.AdminPanelActivity
import com.annimemo.ui.auth.LoginActivity
import com.annimemo.ui.profile.UserProfileActivity
import kotlinx.coroutines.launch

/**
 * Main Dashboard Activity
 * FRS Feature 3: Core Business Module - Pet Management
 * FRS Feature 2: Role-Based Access Control
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var petRecyclerView: RecyclerView
    private lateinit var addPetButton: Button
    private lateinit var adminButton: Button
    private lateinit var profileButton: Button
    private lateinit var logoutButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var tokenManager: TokenManager
    private lateinit var petAdapter: PetAdapter
    
    private val pets = mutableListOf<PetResponse>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        tokenManager = TokenManager(this)
        
        // Check if logged in
        if (!tokenManager.isLoggedIn()) {
            navigateToLogin()
            return
        }
        
        initializeViews()
        setupRecyclerView()
        setupClickListeners()
        loadPets()
    }
    
    private fun initializeViews() {
        petRecyclerView = findViewById(R.id.petRecyclerView)
        addPetButton = findViewById(R.id.buttonAddPet)
        adminButton = findViewById(R.id.buttonAdmin)
        profileButton = findViewById(R.id.buttonProfile)
        logoutButton = findViewById(R.id.buttonLogout)
        progressBar = findViewById(R.id.progressBar)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        
        // Show admin button only for admins - FRS Feature 2: UI-level role restriction
        adminButton.visibility = if (tokenManager.isAdmin()) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }
    
    private fun setupRecyclerView() {
        petAdapter = PetAdapter(pets) { pet ->
            openPetDetails(pet)
        }
        petRecyclerView.layoutManager = LinearLayoutManager(this)
        petRecyclerView.adapter = petAdapter
    }
    
    private fun setupClickListeners() {
        addPetButton.setOnClickListener { navigateToAddPet() }
        adminButton.setOnClickListener { navigateToAdminPanel() }
        profileButton.setOnClickListener { navigateToProfile() }
        logoutButton.setOnClickListener { handleLogout() }
    }
    
    private fun loadPets() {
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val petsFromApi = RetrofitClient.getApiService().getPets()
                pets.clear()
                pets.addAll(petsFromApi)
                petAdapter.notifyDataSetChanged()
                
                updateEmptyState(pets.isEmpty())
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to load pets: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                updateEmptyState(true)
            } finally {
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        emptyStateLayout.visibility = if (isEmpty) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
        petRecyclerView.visibility = if (isEmpty) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }
    
    private fun openPetDetails(pet: PetResponse) {
        val intent = Intent(this, PetListActivity::class.java)
        intent.putExtra("pet_id", pet.id)
        startActivity(intent)
    }
    
    private fun navigateToAddPet() {
        startActivity(Intent(this, AddPetActivity::class.java))
    }
    
    private fun navigateToAdminPanel() {
        startActivity(Intent(this, AdminPanelActivity::class.java))
    }
    
    private fun navigateToProfile() {
        startActivity(Intent(this, UserProfileActivity::class.java))
    }
    
    private fun handleLogout() {
        tokenManager.clearAll()
        navigateToLogin()
    }
    
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    
    override fun onResume() {
        super.onResume()
        // Reload pets when returning to this activity
        loadPets()
    }
}
