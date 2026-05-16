package com.annimemo.ui.health

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.annimemo.R
import com.annimemo.data.model.HealthMetricRequest
import com.annimemo.network.RetrofitClient
import kotlinx.coroutines.launch

/**
 * Add Health Metric Activity
 * FRS Feature 3: Add health record
 */
class AddHealthMetricActivity : AppCompatActivity() {
    
    private lateinit var typeEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var unitEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var progressBar: ProgressBar
    
    private var petId: Int = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_health_metric)
        
        petId = intent.getIntExtra("pet_id", 0)
        
        if (petId == 0) {
            Toast.makeText(this, "Pet ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initializeViews()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        typeEditText = findViewById(R.id.editTextMetricType)
        valueEditText = findViewById(R.id.editTextValue)
        unitEditText = findViewById(R.id.editTextUnit)
        notesEditText = findViewById(R.id.editTextNotes)
        saveButton = findViewById(R.id.buttonSave)
        cancelButton = findViewById(R.id.buttonCancel)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun setupClickListeners() {
        saveButton.setOnClickListener { handleSave() }
        cancelButton.setOnClickListener { finish() }
    }
    
    private fun handleSave() {
        val type = typeEditText.text.toString().trim()
        val valueStr = valueEditText.text.toString().trim()
        val unit = unitEditText.text.toString().trim()
        val notes = notesEditText.text.toString().trim()
        
        if (!validateInput(type, valueStr, unit)) {
            return
        }
        
        val value = valueStr.toDoubleOrNull() ?: 0.0
        
        saveButton.isEnabled = false
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val healthMetricRequest = HealthMetricRequest(
                    metricType = type,
                    value = value,
                    unit = unit,
                    notes = notes,
                    petId = petId
                )
                
                RetrofitClient.getApiService().addHealthMetric(healthMetricRequest)
                
                Toast.makeText(
                    this@AddHealthMetricActivity,
                    "Health record added successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                
            } catch (e: Exception) {
                Toast.makeText(
                    this@AddHealthMetricActivity,
                    "Error adding record: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                saveButton.isEnabled = true
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun validateInput(type: String, value: String, unit: String): Boolean {
        return when {
            type.isEmpty() -> {
                typeEditText.error = "Metric type required"
                false
            }
            value.isEmpty() -> {
                valueEditText.error = "Value required"
                false
            }
            unit.isEmpty() -> {
                unitEditText.error = "Unit required"
                false
            }
            else -> true
        }
    }
}
