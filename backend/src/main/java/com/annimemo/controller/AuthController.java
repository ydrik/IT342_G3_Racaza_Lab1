package com.annimemo.controller;

import com.annimemo.dto.AuthResponse;
import com.annimemo.dto.LoginRequest;
import com.annimemo.dto.MessageResponse;
import com.annimemo.dto.RegisterRequest;
import com.annimemo.dto.UserProfileResponse;
import com.annimemo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * FRS Feature 1: User Authentication (Login, Register)
 * Endpoints:
 * - POST /api/auth/login
 * - POST /api/auth/register
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Get current authenticated user
     * FRS Feature 1: /me endpoint to get current user details
     * @param authentication Current user's authentication
     * @return UserProfileResponse
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            // Delegate to UserService to get profile
            UserProfileResponse profile = authService.getCurrentUser(authentication);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
