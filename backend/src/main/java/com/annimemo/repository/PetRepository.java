package com.annimemo.repository;

import com.annimemo.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Pet entity
 * FRS Feature 2: Pet Health Tracking - Create and Update pet profiles
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByUserId(Long userId);

    Optional<Pet> findByIdAndUserId(Long id, Long userId);
}
