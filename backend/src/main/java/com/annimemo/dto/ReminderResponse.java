package com.annimemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for reminder responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponse {

    private Long id;
    private Long petId;
    private String petName;
    private String title;
    private String type;
    private LocalDate dueDate;
    private String notes;
    private Boolean completed;
    private LocalDateTime createdAt;
}
