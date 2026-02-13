package com.annimemo.controller;

import com.annimemo.dto.HealthMetricRequest;
import com.annimemo.dto.HealthMetricResponse;
import com.annimemo.dto.MessageResponse;
import com.annimemo.service.HealthMetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Health Metric Controller
 * FRS Feature 2: Log Health Metrics (Weight, Medications, Vaccinations, Vet Visits)
 * Endpoints:
 * - GET /api/pets/{petId}/health
 * - GET /api/pets/{petId}/health?type={type}
 * - POST /api/pets/{petId}/health
 * - GET /api/activities/recent
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class HealthMetricController {

    private final HealthMetricService healthMetricService;

    @GetMapping("/api/pets/{petId}/health")
    public ResponseEntity<?> getHealthMetrics(@PathVariable Long petId,
                                             @RequestParam(required = false) String type,
                                             Authentication authentication) {
        try {
            List<HealthMetricResponse> metrics;
            if (type != null && !type.isEmpty()) {
                metrics = healthMetricService.getMetricsByPetAndType(petId, type, authentication);
            } else {
                metrics = healthMetricService.getAllMetricsByPet(petId, authentication);
            }
            return ResponseEntity.ok(metrics);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/api/pets/{petId}/health")
    public ResponseEntity<?> addHealthMetric(@PathVariable Long petId,
                                            @Valid @RequestBody HealthMetricRequest request,
                                            Authentication authentication) {
        try {
            HealthMetricResponse metric = healthMetricService.addHealthMetric(petId, request, authentication);
            return ResponseEntity.ok(metric);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/api/activities/recent")
    public ResponseEntity<?> getRecentActivities(Authentication authentication) {
        try {
            List<HealthMetricResponse> metrics = healthMetricService.getRecentMetricsByUser(authentication);
            return ResponseEntity.ok(metrics);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
