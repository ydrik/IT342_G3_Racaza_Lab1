package com.annimemo.ui.pet

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.annimemo.R
import com.annimemo.data.model.PetRequest
import com.annimemo.data.model.PetResponse
import com.annimemo.network.RetrofitClient
import kotlinx.coroutines.launch

/**
 * Edit Pet Activity
 * FRS Feature 3: Update Pet (CRUD - U)
 */
class EditPetActivity : AppCompatActivity() {
    
    private lateinit var nameEditText: EditText
    private lateinit var speciesEditText: EditText
    private lateinit var breedEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var cancelButton: Button
    private lateinit var progressBar: ProgressBar
    
    private var petId: Int = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pet)
        
        petId = intent.getIntExtra("pet_id", 0)
        
        if (petId == 0) {
            Toast.makeText(this, "Pet ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initializeViews()
        setupClickListeners()
        loadPetData()
    }
    
    private fun initializeViews() {
        nameEditText = findViewById(R.id.editTextName)
        speciesEditText = findViewById(R.id.editTextSpecies)
        breedEditText = findViewById(R.id.editTextBreed)
        ageEditText = findViewById(R.id.editTextAge)
        weightEditText = findViewById(R.id.editTextWeight)
        colorEditText = findViewById(R.id.editTextColor)
        updateButton = findViewById(R.id.buttonSave)
        deleteButton = findViewById(R.id.buttonDelete)
        cancelButton = findViewById(R.id.buttonCancel)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun setupClickListeners() {
        updateButton.setOnClickListener { handleUpdate() }
        deleteButton.setOnClickListener { handleDelete() }
        cancelButton.setOnClickListener { finish() }
    }
    
    private fun loadPetData() {
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val pet = RetrofitClient.getApiService().getPetById(petId)
                populateFormFields(pet)
            } catch (e: Exception) {
                Toast.makeText(
                    this@EditPetActivity,
                    "Error loading pet: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun populateFormFields(pet: PetResponse) {
        nameEditText.setText(pet.name)
        speciesEditText.setText(pet.species)
        breedEditText.setText(pet.breed ?: "")
        ageEditText.setText(pet.age?.toString() ?: "")
        weightEditText.setText(pet.weight?.toString() ?: "")
        colorEditText.setText(pet.color ?: "")
    }
    
    private fun handleUpdate() {
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
        
        updateButton.isEnabled = false
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
                
                RetrofitClient.getApiService().updatePet(petId, petRequest)
                
                Toast.makeText(this@EditPetActivity, "Pet updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
                
            } catch (e: Exception) {
                Toast.makeText(
                    this@EditPetActivity,
                    "Error updating pet: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                updateButton.isEnabled = true
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun handleDelete() {
        updateButton.isEnabled = false
        deleteButton.isEnabled = false
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                RetrofitClient.getApiService().deletePet(petId)
                Toast.makeText(this@EditPetActivity, "Pet deleted successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(
                    this@EditPetActivity,
                    "Error deleting pet: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                updateButton.isEnabled = true
                deleteButton.isEnabled = true
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
