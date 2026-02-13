package com.annimemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for health metric responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthMetricResponse {

    private Long id;
    private Long petId;
    private String petName;
    private String type;
    private LocalDate date;
    private Double weight;
    private String medicationName;
    private String dosage;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String vaccineName;
    private LocalDate vaccineDate;
    private LocalDate nextDue;
    private String veterinarian;
    private String visitReason;
    private String diagnosis;
    private String treatment;
    private String notes;
}
