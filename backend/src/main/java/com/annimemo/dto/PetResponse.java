package com.annimemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for pet responses
 * FRS Feature 2: Pet Health Tracking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {

    private Long id;
    private String name;
    private String species;
    private String breed;
    private LocalDate dateOfBirth;
    private String gender;
    private Double weight;
    private String color;
    private String imageUrl;
    private String notes;
    private LocalDateTime createdAt;
}
