package com.annimemo.service;

import com.annimemo.dto.PetRequest;
import com.annimemo.dto.PetResponse;
import com.annimemo.model.Pet;
import com.annimemo.model.User;
import com.annimemo.repository.PetRepository;
import com.annimemo.repository.UserRepository;
import com.annimemo.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pet Service
 * FRS Feature 2: Create and Update Pet Profiles
 */
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public List<PetResponse> getAllPetsByUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Pet> pets = petRepository.findByUserId(user.getId());

        return pets.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PetResponse getPetById(Long petId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = petRepository.findByIdAndUserId(petId, user.getId())
                .orElseThrow(() -> new RuntimeException("Pet not found or access denied"));

        return convertToResponse(pet);
    }

    @Transactional
    public PetResponse createPet(PetRequest petRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = new Pet();
        pet.setUser(user);
        pet.setName(petRequest.getName());
        pet.setSpecies(petRequest.getSpecies());
        pet.setBreed(petRequest.getBreed());
        pet.setDateOfBirth(petRequest.getDateOfBirth());
        pet.setGender(petRequest.getGender());
        pet.setWeight(petRequest.getWeight());
        pet.setColor(petRequest.getColor());
        pet.setImageUrl(petRequest.getImageUrl());
        pet.setNotes(petRequest.getNotes());
        pet.setCreatedAt(LocalDateTime.now());
        pet.setUpdatedAt(LocalDateTime.now());

        Pet savedPet = petRepository.save(pet);

        return convertToResponse(savedPet);
    }

    @Transactional
    public PetResponse updatePet(Long petId, PetRequest petRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = petRepository.findByIdAndUserId(petId, user.getId())
                .orElseThrow(() -> new RuntimeException("Pet not found or access denied"));

        pet.setName(petRequest.getName());
        pet.setSpecies(petRequest.getSpecies());
        pet.setBreed(petRequest.getBreed());
        pet.setDateOfBirth(petRequest.getDateOfBirth());
        pet.setGender(petRequest.getGender());
        pet.setWeight(petRequest.getWeight());
        pet.setColor(petRequest.getColor());
        pet.setImageUrl(petRequest.getImageUrl());
        pet.setNotes(petRequest.getNotes());
        pet.setUpdatedAt(LocalDateTime.now());

        Pet updatedPet = petRepository.save(pet);

        return convertToResponse(updatedPet);
    }

    @Transactional
    public void deletePet(Long petId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = petRepository.findByIdAndUserId(petId, user.getId())
                .orElseThrow(() -> new RuntimeException("Pet not found or access denied"));

        petRepository.delete(pet);
    }

    private PetResponse convertToResponse(Pet pet) {
        PetResponse response = new PetResponse();
        response.setId(pet.getId());
        response.setName(pet.getName());
        response.setSpecies(pet.getSpecies());
        response.setBreed(pet.getBreed());
        response.setDateOfBirth(pet.getDateOfBirth());
        response.setGender(pet.getGender());
        response.setWeight(pet.getWeight());
        response.setColor(pet.getColor());
        response.setImageUrl(pet.getImageUrl());
        response.setNotes(pet.getNotes());
                response.setCreatedAt(pet.getCreatedAt());
        return response;
    }
}
