package com.annimemo.service;

import com.annimemo.dto.UserProfileResponse;
import com.annimemo.model.Role;
import com.annimemo.model.User;
import com.annimemo.repository.PetRepository;
import com.annimemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Admin Service
 * FRS Feature 2: Role-Based Access Control
 * Service for admin-only operations
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    /**
     * Get all users in the system
     * @return List of UserProfileResponse
     */
    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserProfileResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Update user role
     * @param userId User ID
     * @param newRole New role to assign
     * @return Updated user profile
     */
    @Transactional
    public UserProfileResponse updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);

        return new UserProfileResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail()
        );
    }

    /**
     * Delete user (soft delete by marking as inactive)
     * @param userId User ID
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Soft delete
        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Get system statistics
     * @return Map with system stats
     */
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalPets", petRepository.count());
        stats.put("activeUsers", userRepository.findAll().stream()
                .filter(User::getActive)
                .count());
        return stats;
    }
}
