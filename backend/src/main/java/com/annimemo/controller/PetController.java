package com.annimemo.controller;

import com.annimemo.dto.MessageResponse;
import com.annimemo.dto.PetRequest;
import com.annimemo.dto.PetResponse;
import com.annimemo.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pet Controller
 * FRS Feature 2: Create and Update Pet Profiles
 * Endpoints:
 * - GET /api/pets
 * - GET /api/pets/{id}
 * - POST /api/pets
 * - PUT /api/pets/{id}
 * - DELETE /api/pets/{id}
 */
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<PetResponse>> getAllPets(Authentication authentication) {
        List<PetResponse> pets = petService.getAllPetsByUser(authentication);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Long id, Authentication authentication) {
        try {
            PetResponse pet = petService.getPetById(id, authentication);
            return ResponseEntity.ok(pet);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPet(@Valid @RequestBody PetRequest petRequest,
                                      Authentication authentication) {
        try {
            PetResponse createdPet = petService.createPet(petRequest, authentication);
            return ResponseEntity.ok(createdPet);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePet(@PathVariable Long id,
                                      @Valid @RequestBody PetRequest petRequest,
                                      Authentication authentication) {
        try {
            PetResponse updatedPet = petService.updatePet(id, petRequest, authentication);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id, Authentication authentication) {
        try {
            petService.deletePet(id, authentication);
            return ResponseEntity.ok(new MessageResponse("Pet deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
