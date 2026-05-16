package com.annimemo.ui.pet

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.annimemo.R
import com.annimemo.data.model.PetResponse
import com.bumptech.glide.Glide

/**
 * Pet Adapter for RecyclerView
 * FRS Feature 3: Display Pet List
 */
class PetAdapter(
    private val pets: List<PetResponse>,
    private val onPetClick: (PetResponse) -> Unit
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view, onPetClick)
    }
    
    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(pets[position])
    }
    
    override fun getItemCount(): Int = pets.size
    
    class PetViewHolder(
        itemView: View,
        private val onPetClick: (PetResponse) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val petImageView: ImageView = itemView.findViewById(R.id.petImage)
        private val petNameTextView: TextView = itemView.findViewById(R.id.petName)
        private val petBreedTextView: TextView = itemView.findViewById(R.id.petBreed)
        private val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        private val healthButton: Button = itemView.findViewById(R.id.buttonHealth)
        
        fun bind(pet: PetResponse) {
            petNameTextView.text = pet.name
            petBreedTextView.text = "${pet.species}${pet.breed?.let { " • $it" } ?: ""}"
            
            // Load image
            if (!pet.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(pet.imageUrl)
                    .centerCrop()
                    .into(petImageView)
            } else {
                // Set placeholder based on species
                val placeholder = when (pet.species) {
                    "Dog" -> "🐕"
                    "Cat" -> "🐱"
                    "Bird" -> "🦜"
                    "Fish" -> "🐟"
                    "Rabbit" -> "🐰"
                    else -> "🐾"
                }
                petNameTextView.text = "($placeholder) ${pet.name}"
            }
            
            editButton.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, EditPetActivity::class.java)
                intent.putExtra("pet_id", pet.id)
                context.startActivity(intent)
            }
            
            healthButton.setOnClickListener {
                onPetClick(pet)
            }
            
            itemView.setOnClickListener { onPetClick(pet) }
        }
    }
}
