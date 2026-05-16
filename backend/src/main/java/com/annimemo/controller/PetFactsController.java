package com.annimemo.controller;

import com.annimemo.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Pet Facts Controller
 * Provides pet facts for dog/cat/any via external public APIs.
 */
@RestController
@RequestMapping("/api/facts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PetFactsController {

    private final ExternalApiService externalApiService;

    @GetMapping("/random")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getRandomFact(
            @RequestParam(defaultValue = "any") String species
    ) {
        return ResponseEntity.ok(externalApiService.getRandomPetFact(species));
    }

    @GetMapping("/random-list")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getRandomFacts(
            @RequestParam(defaultValue = "any") String species,
            @RequestParam(defaultValue = "5") int count
    ) {
        int safeCount = Math.max(1, Math.min(count, 20));
        List<Map<String, Object>> facts = new ArrayList<>();

        for (int i = 0; i < safeCount; i++) {
            facts.add(externalApiService.getRandomPetFact(species));
        }

        return ResponseEntity.ok(facts);
    }
}
