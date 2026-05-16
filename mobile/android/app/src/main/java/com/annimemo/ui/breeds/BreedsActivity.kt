package com.annimemo.ui.breeds

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.annimemo.R
import com.annimemo.network.RetrofitClient
import kotlinx.coroutines.launch

/**
 * Simple breeds explorer screen.
 * This fulfills the manifest activity reference and provides a working API-backed UI.
 */
class BreedsActivity : AppCompatActivity() {

    private lateinit var speciesSpinner: Spinner
    private lateinit var loadButton: Button
    private lateinit var backButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var resultsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breeds)

        RetrofitClient.initialize(this)

        speciesSpinner = findViewById(R.id.speciesSpinner)
        loadButton = findViewById(R.id.loadBreedsButton)
        backButton = findViewById(R.id.backButton)
        progressBar = findViewById(R.id.progressBar)
        resultsText = findViewById(R.id.resultsText)

        speciesSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Dogs", "Cats")
        )

        loadButton.setOnClickListener { loadBreeds() }
        backButton.setOnClickListener { finish() }
    }

    private fun loadBreeds() {
        setLoading(true)
        lifecycleScope.launch {
            try {
                val isDogs = speciesSpinner.selectedItemPosition == 0
                val items = if (isDogs) {
                    RetrofitClient.getApiService().getDogBreeds()
                } else {
                    RetrofitClient.getApiService().getCatBreeds()
                }

                if (items.isEmpty()) {
                    resultsText.text = "No breeds returned by server."
                } else {
                    val names = items
                        .mapNotNull { item -> item["name"]?.toString() }
                        .take(50)
                    resultsText.text = names.joinToString(separator = "\n")
                }
            } catch (ex: Exception) {
                Toast.makeText(this@BreedsActivity, "Failed to load breeds: ${ex.message}", Toast.LENGTH_SHORT).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        loadButton.isEnabled = !isLoading
    }
}
