package com.annimemo.service;

import com.annimemo.dto.LoginRequest;
import com.annimemo.dto.RegisterRequest;
import com.annimemo.dto.AuthResponse;
import com.annimemo.dto.UserProfileResponse;
import com.annimemo.model.User;
import com.annimemo.repository.UserRepository;
import com.annimemo.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Authentication Service
 * FRS Feature 1: User Authentication (Login, Register)
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final EmailService emailService;

    public AuthResponse login(LoginRequest loginRequest) {
        String identifier = normalizeIdentifier(loginRequest.getIdentifier());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                identifier,
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        String resolvedUsername = authentication.getName();
        User user = userRepository.findByUsername(resolvedUsername)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        return new AuthResponse(jwt, user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        String normalizedUsername = normalizeUsername(registerRequest.getUsername());
        String normalizedEmail = normalizeEmail(registerRequest.getEmail());
        String normalizedFirstName = normalizeName(registerRequest.getFirstName(), "First name");
        String normalizedLastName = normalizeName(registerRequest.getLastName(), "Last name");

        validateNormalizedRegistrationData(normalizedUsername, normalizedFirstName, normalizedLastName, normalizedEmail, registerRequest.getPassword());

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new RuntimeException("Email is already in use");
        }

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(normalizedFirstName);
        user.setLastName(normalizedLastName);
        user.setEmail(normalizedEmail);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Automatically login after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                normalizedUsername,
                        registerRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Send welcome email
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername(), savedUser.getFirstName());

        return new AuthResponse(jwt, savedUser.getId(), savedUser.getUsername(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail());
    }

    /**
     * Get current authenticated user details
     * FRS Feature 1: /me endpoint
     * @param authentication Current user's authentication
     * @return UserProfileResponse
     */
    public UserProfileResponse getCurrentUser(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    private String normalizeIdentifier(String identifier) {
        return identifier == null ? "" : identifier.trim();
    }

    private String normalizeUsername(String username) {
        return username == null ? "" : username.trim();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeName(String value, String fieldName) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new RuntimeException(fieldName + " is required");
        }
        return normalized;
    }

    private void validateNormalizedRegistrationData(
            String username,
            String firstName,
            String lastName,
            String email,
            String password
    ) {
        if (username.length() < 3 || username.length() > 30) {
            throw new RuntimeException("Username must be between 3 and 30 characters");
        }
        if (firstName.length() > 50) {
            throw new RuntimeException("First name must be at most 50 characters");
        }
        if (lastName.length() > 50) {
            throw new RuntimeException("Last name must be at most 50 characters");
        }
        if (email.length() > 120) {
            throw new RuntimeException("Email must be at most 120 characters");
        }
        if (password == null || password.length() < 12 || password.length() > 100) {
            throw new RuntimeException("Password must be between 12 and 100 characters");
        }
    }
}
