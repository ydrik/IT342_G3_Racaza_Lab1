package com.annimemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * HealthMetric Entity
 * FRS Feature 2: Log Health Metrics (e.g., weight, medication)
 * Represents health records for pets
 */
@Entity
@Table(name = "health_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // weight, medication, vaccination, vetVisit

    @Column(nullable = false)
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

    @Column(length = 2000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @JsonIgnore
    private Pet pet;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
