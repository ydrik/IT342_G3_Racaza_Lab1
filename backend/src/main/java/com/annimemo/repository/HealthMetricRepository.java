package com.annimemo.repository;

import com.annimemo.model.HealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for HealthMetric entity
 * FRS Feature 2: Log Health Metrics (e.g., weight, medication)
 */
@Repository
public interface HealthMetricRepository extends JpaRepository<HealthMetric, Long> {

    List<HealthMetric> findByPetIdOrderByDateDesc(Long petId);

    List<HealthMetric> findByPetIdAndTypeOrderByDateDesc(Long petId, String type);

    @Query("SELECT h FROM HealthMetric h WHERE h.pet.user.id = :userId ORDER BY h.createdAt DESC")
    List<HealthMetric> findRecentByUserId(Long userId);
}
