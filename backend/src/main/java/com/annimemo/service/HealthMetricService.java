package com.annimemo.service;

import com.annimemo.dto.HealthMetricRequest;
import com.annimemo.dto.HealthMetricResponse;
import com.annimemo.model.HealthMetric;
import com.annimemo.model.Pet;
import com.annimemo.model.User;
import com.annimemo.repository.HealthMetricRepository;
import com.annimemo.repository.PetRepository;
import com.annimemo.repository.UserRepository;
import com.annimemo.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Health Metric Service
 * FRS Feature 2: Log Health Metrics (Weight, Medications, Vaccinations, Vet Visits)
 */
@Service
@RequiredArgsConstructor
public class HealthMetricService {

    private final HealthMetricRepository healthMetricRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public List<HealthMetricResponse> getAllMetricsByPet(Long petId, Authentication authentication) {
        validatePetOwnership(petId, authentication);

        List<HealthMetric> metrics = healthMetricRepository.findByPetIdOrderByDateDesc(petId);

        return metrics.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<HealthMetricResponse> getMetricsByPetAndType(Long petId, String type, Authentication authentication) {
        validatePetOwnership(petId, authentication);

        List<HealthMetric> metrics = healthMetricRepository.findByPetIdAndTypeOrderByDateDesc(petId, type);

        return metrics.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<HealthMetricResponse> getRecentMetricsByUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<HealthMetric> metrics = healthMetricRepository.findRecentByUserId(user.getId());

        return metrics.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HealthMetricResponse addHealthMetric(Long petId, HealthMetricRequest request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = petRepository.findByIdAndUserId(petId, user.getId())
                .orElseThrow(() -> new RuntimeException("Pet not found or access denied"));

        HealthMetric metric = new HealthMetric();
        metric.setPet(pet);
        metric.setType(request.getType());
        metric.setDate(request.getDate());
        metric.setNotes(request.getNotes());

        // Set type-specific fields
        switch (request.getType().toLowerCase()) {
            case "weight":
                metric.setWeight(request.getWeight());
                break;
            case "medication":
                metric.setMedicationName(request.getMedicationName());
                metric.setDosage(request.getDosage());
                metric.setFrequency(request.getFrequency());
                metric.setStartDate(request.getStartDate());
                metric.setEndDate(request.getEndDate());
                break;
            case "vaccination":
                metric.setVaccineName(request.getVaccineName());
                metric.setVaccineDate(request.getVaccineDate());
                metric.setNextDue(request.getNextDue());
                metric.setVeterinarian(request.getVeterinarian());
                break;
            case "vet_visit":
                metric.setVisitReason(request.getVisitReason());
                metric.setDiagnosis(request.getDiagnosis());
                metric.setTreatment(request.getTreatment());
                metric.setVeterinarian(request.getVeterinarian());
                break;
            default:
                throw new RuntimeException("Invalid health metric type");
        }

        HealthMetric savedMetric = healthMetricRepository.save(metric);

        return convertToResponse(savedMetric);
    }

    private void validatePetOwnership(Long petId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        petRepository.findByIdAndUserId(petId, user.getId())
                .orElseThrow(() -> new RuntimeException("Pet not found or access denied"));
    }

    private HealthMetricResponse convertToResponse(HealthMetric metric) {
        HealthMetricResponse response = new HealthMetricResponse();
        response.setId(metric.getId());
        response.setPetId(metric.getPet().getId());
        response.setPetName(metric.getPet().getName());
        response.setType(metric.getType());
        response.setDate(metric.getDate());
        response.setNotes(metric.getNotes());

        // Weight fields
        response.setWeight(metric.getWeight());

        // Medication fields
        response.setMedicationName(metric.getMedicationName());
        response.setDosage(metric.getDosage());
        response.setFrequency(metric.getFrequency());
        response.setStartDate(metric.getStartDate());
        response.setEndDate(metric.getEndDate());

        // Vaccination fields
        response.setVaccineName(metric.getVaccineName());
        response.setVaccineDate(metric.getVaccineDate());
        response.setNextDue(metric.getNextDue());
        response.setVeterinarian(metric.getVeterinarian());

        // Vet visit fields
        response.setVisitReason(metric.getVisitReason());
        response.setDiagnosis(metric.getDiagnosis());
        response.setTreatment(metric.getTreatment());

        return response;
    }
}
