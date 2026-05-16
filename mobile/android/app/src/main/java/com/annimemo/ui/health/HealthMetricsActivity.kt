package com.annimemo.ui.health

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
import com.annimemo.data.model.HealthMetricResponse
import com.annimemo.network.RetrofitClient
import kotlinx.coroutines.launch

/**
 * Health Metrics Activity
 * FRS Feature 3: Health Records
 */
class HealthMetricsActivity : AppCompatActivity() {
    
    private lateinit var metricsRecyclerView: RecyclerView
    private lateinit var addMetricButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyStateLayout: LinearLayout
    
    private var petId: Int = 0
    private val metrics = mutableListOf<HealthMetricResponse>()
    private lateinit var metricsAdapter: HealthMetricsAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_metrics)
        
        petId = intent.getIntExtra("pet_id", 0)
        
        if (petId == 0) {
            Toast.makeText(this, "Pet ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initializeViews()
        setupRecyclerView()
        setupClickListeners()
        loadHealthMetrics()
    }
    
    private fun initializeViews() {
        metricsRecyclerView = findViewById(R.id.healthMetricsRecyclerView)
        addMetricButton = findViewById(R.id.buttonAddMetric)
        progressBar = findViewById(R.id.progressBar)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
    }
    
    private fun setupRecyclerView() {
        metricsAdapter = HealthMetricsAdapter(metrics)
        metricsRecyclerView.layoutManager = LinearLayoutManager(this)
        metricsRecyclerView.adapter = metricsAdapter
    }
    
    private fun setupClickListeners() {
        addMetricButton.setOnClickListener {
            val intent = Intent(this, AddHealthMetricActivity::class.java)
            intent.putExtra("pet_id", petId)
            startActivity(intent)
        }
    }
    
    private fun loadHealthMetrics() {
        progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val metricsFromApi = RetrofitClient.getApiService().getHealthMetrics(petId)
                metrics.clear()
                metrics.addAll(metricsFromApi)
                metricsAdapter.notifyDataSetChanged()
                
                updateEmptyState(metrics.isEmpty())
            } catch (e: Exception) {
                Toast.makeText(
                    this@HealthMetricsActivity,
                    "Failed to load metrics: ${e.message}",
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
        metricsRecyclerView.visibility = if (isEmpty) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Reload metrics when returning to this activity
        loadHealthMetrics()
    }
}
