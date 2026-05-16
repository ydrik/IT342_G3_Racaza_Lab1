package com.annimemo.ui.pet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.annimemo.R
import com.annimemo.data.model.PetRequest
import com.annimemo.network.RetrofitClient
import kotlinx.coroutines.launch

/**
 * Add Pet Activity
 * FRS Feature 3: Create Pet (CRUD - C)
 */
class AddPetActivity : AppCompatActivity() {
    
    private lateinit var nameEditText: EditText
    private lateinit var speciesEditText: EditText
    private lateinit var breedEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var progressBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)
        
        initializeViews()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        nameEditText = findViewById(R.id.editTextName)
        speciesEditText = findViewById(R.id.editTextSpecies)
        breedEditText = findViewById(R.id.editTextBreed)
        ageEditText = findViewById(R.id.editTextAge)
        weightEditText = findViewById(R.id.editTextWeight)
        colorEditText = findViewById(R.id.editTextColor)
        saveButton = findViewById(R.id.buttonSave)
        cancelButton = findViewById(R.id.buttonCancel)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun setupClickListeners() {
        saveButton.setOnClickListener { handleSave() }
        cancelButton.setOnClickListener { finish() }
    }
    
    private fun handleSave() {
        val name = nameEditText.text.toString().trim()
        val species = speciesEditText.text.toString().trim()
        val breed = breedEditText.text.toString().trim()
        val ageStr = ageEditText.text.toString().trim()
        val weightStr = weightEditText.text.toString().trim()
        val color = colorEditText.text.toString().trim()
        
        if (!validateInput(name, species, breed, ageStr, weightStr)) {
            return
        }
        
        val age = ageStr.toIntOrNull() ?: 0
        val weight = weightStr.toDoubleOrNull() ?: 0.0
        
        saveButton.isEnabled = false
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val petRequest = PetRequest(
                    name = name,
                    species = species,
                    breed = breed,
                    age = age,
                    weight = weight,
                    color = color
                )
                
                RetrofitClient.getApiService().createPet(petRequest)
                
                Toast.makeText(this@AddPetActivity, "Pet added successfully!", Toast.LENGTH_SHORT).show()
                finish()
                
            } catch (e: Exception) {
                Toast.makeText(
                    this@AddPetActivity,
                    "Error adding pet: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                saveButton.isEnabled = true
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun validateInput(
        name: String, species: String, breed: String,
        age: String, weight: String
    ): Boolean {
        return when {
            name.isEmpty() -> {
                nameEditText.error = "Name required"
                false
            }
            species.isEmpty() -> {
                speciesEditText.error = "Species required"
                false
            }
            breed.isEmpty() -> {
                breedEditText.error = "Breed required"
                false
            }
            age.isEmpty() -> {
                ageEditText.error = "Age required"
                false
            }
            weight.isEmpty() -> {
                weightEditText.error = "Weight required"
                false
            }
            else -> true
        }
    }
}
