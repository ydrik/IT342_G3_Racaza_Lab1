package com.annimemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user profile responses
 * FRS Feature 1: View Profile Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
