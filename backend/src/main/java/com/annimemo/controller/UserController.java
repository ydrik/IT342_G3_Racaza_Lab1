package com.annimemo.controller;

import com.annimemo.dto.ChangePasswordRequest;
import com.annimemo.dto.MessageResponse;
import com.annimemo.dto.UpdateProfileRequest;
import com.annimemo.dto.UserProfileResponse;
import com.annimemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 * FRS Feature 1: View and Update Profile Information
 * Endpoints:
 * - GET /api/users/profile
 * - PUT /api/users/profile
 * - PUT /api/users/password
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        UserProfileResponse profile = userService.getProfile(authentication);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest updateRequest,
                                          Authentication authentication) {
        try {
            UserProfileResponse updatedProfile = userService.updateProfile(updateRequest, authentication);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                           Authentication authentication) {
        try {
            userService.changePassword(changePasswordRequest, authentication);
            return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
