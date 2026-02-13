package com.annimemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating health metric records
 * FRS Feature 2: Log Health Metrics (e.g., weight, medication)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthMetricRequest {

    @NotBlank(message = "Type is required")
    private String type; // weight, medication, vaccination, vetVisit

    @NotNull(message = "Date is required")
    private LocalDate date;

    // Weight tracking
    private Double weight;

    // Medication
    private String medicationName;
    private String dosage;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;

    // Vaccination
    private String vaccineName;
    private LocalDate vaccineDate;
    private LocalDate nextDue;
    private String veterinarian;

    // Vet Visit
    private String visitReason;
    private String diagnosis;
    private String treatment;

    private String notes;
}
