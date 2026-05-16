package com.annimemo.ui.health

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.annimemo.R
import com.annimemo.data.model.HealthMetricResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Health Metrics Adapter for RecyclerView
 * FRS Feature 3: Display Health Records
 */
class HealthMetricsAdapter(
    private val metrics: List<HealthMetricResponse>
) : RecyclerView.Adapter<HealthMetricsAdapter.MetricViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetricViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_health_metric, parent, false)
        return MetricViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: MetricViewHolder, position: Int) {
        holder.bind(metrics[position])
    }
    
    override fun getItemCount(): Int = metrics.size
    
    class MetricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        
        private val typeTextView: TextView = itemView.findViewById(R.id.metricType)
        private val valueTextView: TextView = itemView.findViewById(R.id.metricValue)
        private val dateTextView: TextView = itemView.findViewById(R.id.metricDate)
        private val notesTextView: TextView = itemView.findViewById(R.id.metricNotes)
        
        fun bind(metric: HealthMetricResponse) {
            typeTextView.text = metric.metricType
            valueTextView.text = "${metric.value} ${metric.unit}"
            
            // Format date
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                val date = inputFormat.parse(metric.recordedAt ?: "")
                dateTextView.text = if (date != null) outputFormat.format(date) else metric.recordedAt
            } catch (e: Exception) {
                dateTextView.text = metric.recordedAt
            }
            
            notesTextView.visibility = if (metric.notes?.isNotEmpty() == true) {
                View.VISIBLE
            } else {
                View.GONE
            }
            notesTextView.text = metric.notes ?: ""
        }
    }
}
