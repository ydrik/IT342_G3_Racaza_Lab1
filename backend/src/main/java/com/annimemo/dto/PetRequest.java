package com.annimemo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating/updating pet profiles
 * FRS Feature 2: Create and Update pet profiles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetRequest {

    @NotBlank(message = "Pet name is required")
    private String name;

    @NotBlank(message = "Species is required")
    private String species;

    private String breed;
    private LocalDate dateOfBirth;
    private String gender;
    private Double weight;
    private String color;
    private String imageUrl;
    private String notes;
}
