package com.annimemo.controller;

import com.annimemo.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Pet Breeds Controller
 * FRS Feature 4.1: External API Integration
 * Provides endpoints to fetch breed information from external APIs
 */
@RestController
@RequestMapping("/api/breeds")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BreedController {

    private final ExternalApiService externalApiService;

    /**
     * Get all dog breeds
     * FRS Feature 4.1: Display data from external API
     */
    @GetMapping("/dogs")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getDogBreeds() {
        List<Map<String, Object>> breeds = externalApiService.getDogBreeds();
        return ResponseEntity.ok(breeds);
    }

    /**
     * Get all cat breeds
     * FRS Feature 4.1: Display data from external API
     */
    @GetMapping("/cats")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getCatBreeds() {
        List<Map<String, Object>> breeds = externalApiService.getCatBreeds();
        return ResponseEntity.ok(breeds);
    }

    /**
     * Search dog breed by name
     * FRS Feature 4.1: Display data from external API
     */
    @GetMapping("/dogs/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> searchDogBreed(@RequestParam String name) {
        Map<String, Object> breed = externalApiService.searchDogBreed(name);
        return ResponseEntity.ok(breed);
    }

    /**
     * Search cat breed by name
     * FRS Feature 4.1: Display data from external API
     */
    @GetMapping("/cats/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> searchCatBreed(@RequestParam String name) {
        Map<String, Object> breed = externalApiService.searchCatBreed(name);
        return ResponseEntity.ok(breed);
    }

    /**
     * Get random dog image
     * FRS Feature 4.1: External API integration
     */
    @GetMapping("/dogs/random-image")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getRandomDogImage() {
        Map<String, Object> image = externalApiService.getRandomDogImage();
        return ResponseEntity.ok(image);
    }

    /**
     * Get random cat image
     * FRS Feature 4.1: External API integration
     */
    @GetMapping("/cats/random-image")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getRandomCatImage() {
        Map<String, Object> image = externalApiService.getRandomCatImage();
        return ResponseEntity.ok(image);
    }

    /**
     * Get all breeds for any supported species.
     * Dogs and cats use live external APIs; other species use curated static data.
     * FRS Feature 4.1: Species-agnostic breed lookup
     */
    @GetMapping("/{species}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getBreedsBySpecies(@PathVariable String species) {
        List<Map<String, Object>> breeds = externalApiService.getBreedsBySpecies(species);
        return ResponseEntity.ok(breeds);
    }

    /**
     * Search breed by species and name.
     * Dogs and cats use live external APIs; other species search through static data.
     * FRS Feature 4.1: Species-agnostic breed search
     */
    @GetMapping("/{species}/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> searchBreedBySpecies(
            @PathVariable String species,
            @RequestParam String name) {
        Map<String, Object> breed = externalApiService.searchBreedBySpecies(species, name);
        return ResponseEntity.ok(breed);
    }
}
