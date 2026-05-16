package com.annimemo.network

import com.annimemo.data.model.*
import retrofit2.http.*

/**
 * Retrofit API Service
 * FRS Feature 7.5: Proper API Integration using Retrofit
 * Defines all API endpoints for communication with Spring Boot backend
 */
interface ApiService {
    
    // ===== FRS Feature 1: Authentication =====
    
    /**
     * User Registration endpoint
     * FRS Feature 1: User Registration
     */
    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
    
    /**
     * User Login endpoint
     * FRS Feature 1: User Login with JWT
     */
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
    
    /**
     * Get current authenticated user
     * FRS Feature 1: /me endpoint
     */
    @GET("/api/users/profile")
    suspend fun getCurrentUser(): UserProfile
    
    
    // ===== FRS Feature 3: Core Business Module - Pet Management =====
    
    /**
     * Get all pets for current user
     * FRS Feature 3: Read Pet Data
     */
    @GET("/api/pets")
    suspend fun getPets(): List<PetResponse>
    
    /**
     * Get single pet by ID
     */
    @GET("/api/pets/{id}")
    suspend fun getPetById(@Path("id") id: Long): PetResponse
    
    /**
     * Create new pet
     * FRS Feature 3: Create Pet (CRUD - C)
     */
    @POST("/api/pets")
    suspend fun createPet(@Body request: PetRequest): PetResponse
    
    /**
     * Update pet
     * FRS Feature 3: Update Pet (CRUD - U)
     */
    @PUT("/api/pets/{id}")
    suspend fun updatePet(@Path("id") id: Long, @Body request: PetRequest): PetResponse
    
    /**
     * Delete pet
     * FRS Feature 3: Delete Pet (CRUD - D)
     */
    @DELETE("/api/pets/{id}")
    suspend fun deletePet(@Path("id") id: Long): MessageResponse
    
    
    // ===== FRS Feature 3: Health Metrics =====
    
    /**
     * Get health metrics for a pet
     * FRS Feature 3: Read Health Data
     */
    @GET("/api/pets/{petId}/health")
    suspend fun getHealthMetrics(@Path("petId") petId: Long): List<HealthMetricResponse>
    
    /**
     * Add health metric
     * FRS Feature 3: Create Health Record (CRUD - C)
     */
    @POST("/api/health-metrics")
    suspend fun addHealthMetric(@Body request: HealthMetricRequest): HealthMetricResponse
    
    /**
     * Update health metric
     * FRS Feature 3: Update Health Record (CRUD - U)
     */
    @PUT("/api/health-metrics/{id}")
    suspend fun updateHealthMetric(@Path("id") id: Long, @Body request: HealthMetricRequest): HealthMetricResponse
    
    /**
     * Delete health metric
     * FRS Feature 3: Delete Health Record (CRUD - D)
     */
    @DELETE("/api/health-metrics/{id}")
    suspend fun deleteHealthMetric(@Path("id") id: Long): MessageResponse
    
    
    // ===== FRS Feature 4.1: External API Integration - Breeds =====
    
    /**
     * Get dog breeds from external API
     * FRS Feature 4.1: External API Integration
     */
    @GET("/api/breeds/dogs")
    suspend fun getDogBreeds(): List<Map<String, Any>>
    
    /**
     * Get cat breeds from external API
     */
    @GET("/api/breeds/cats")
    suspend fun getCatBreeds(): List<Map<String, Any>>
    
    /**
     * Search breed by name
     */
    @GET("/api/breeds/search")
    suspend fun searchBreed(@Query("name") name: String, @Query("type") type: String): List<Map<String, Any>>
    
    
    // ===== FRS Feature 4.3: File Upload =====
    
    /**
     * Upload pet image
     * FRS Feature 4.3: File Upload
     */
    @Multipart
    @POST("/api/files/upload")
    suspend fun uploadFile(@Part file: okhttp3.MultipartBody.Part): FileUploadResponse
    
    /**
     * Delete uploaded file
     */
    @DELETE("/api/files/{fileName}")
    suspend fun deleteFile(@Path("fileName") fileName: String): MessageResponse
    
    
    // ===== FRS Feature 2: Role-Based Access Control - Admin =====
    
    /**
     * Get all users (Admin only)
     * FRS Feature 2: API-level role restriction
     */
    @GET("/api/admin/users")
    suspend fun getAllUsers(): List<UserProfile>
    
    /**
     * Update user role (Admin only)
     */
    @PUT("/api/admin/users/{id}/role")
    suspend fun updateUserRole(@Path("id") id: Long, @Query("role") role: String): UserProfile
    
    /**
     * Delete user (Admin only)
     */
    @DELETE("/api/admin/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long): MessageResponse
    
    /**
     * Get system statistics (Admin only)
     */
    @GET("/api/admin/stats")
    suspend fun getSystemStats(): Map<String, Any>
}
