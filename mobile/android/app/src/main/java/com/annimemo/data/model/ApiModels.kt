package com.annimemo.data.model

import com.google.gson.annotations.SerializedName

// ===== Authentication DTOs =====

data class RegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)

data class LoginRequest(
    val identifier: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: UserProfile
)

// ===== User Model =====

data class UserProfile(
    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String, // "USER" or "ADMIN" - FRS Feature 2: Role-Based Access Control
    val active: Boolean,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)

// ===== Pet Models - FRS Feature 3: Core Business Module =====

data class PetRequest(
    val name: String,
    val species: String,
    val breed: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val weight: Double? = null,
    val color: String? = null,
    val imageUrl: String? = null,
    val notes: String? = null
)

data class PetResponse(
    val id: Long,
    val name: String,
    val species: String,
    val breed: String?,
    val dateOfBirth: String?,
    val gender: String?,
    val weight: Double?,
    val color: String?,
    val imageUrl: String?,
    val notes: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)

// ===== Health Metrics Models - FRS Feature 3 =====

data class HealthMetricRequest(
    val petId: Long,
    val type: String, // weight, vaccination, medication, vet_visit, etc.
    val date: String,
    val weight: Double? = null,
    val vaccineName: String? = null,
    val vaccineDate: String? = null,
    val medicationName: String? = null,
    val dosage: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val visitReason: String? = null,
    val visitDate: String? = null,
    val notes: String? = null
)

data class HealthMetricResponse(
    val id: Long,
    val petId: Long,
    val type: String,
    val date: String,
    val weight: Double?,
    val vaccineName: String?,
    val vaccineDate: String?,
    val medicationName: String?,
    val dosage: String?,
    val startDate: String?,
    val endDate: String?,
    val visitReason: String?,
    val visitDate: String?,
    val notes: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)

// ===== File Upload Response =====

data class FileUploadResponse(
    val fileName: String,
    val fileDownloadUri: String,
    val fileType: String,
    val size: Long
)

// ===== Generic Response =====

data class MessageResponse(
    val message: String
)

// ===== Breed Response - FRS Feature 4.1: External API Integration =====

data class BreedResponse(
    val name: String,
    val temperament: String?,
    val origin: String?,
    val lifeSpan: String?,
    val weight: String?,
    val height: String?
)
