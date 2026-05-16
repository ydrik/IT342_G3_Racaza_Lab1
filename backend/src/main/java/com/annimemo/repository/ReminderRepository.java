package com.annimemo.repository;

import com.annimemo.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Reminder entity.
 */
@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByUserIdOrderByDueDateAsc(Long userId);

    List<Reminder> findByUserIdAndCompletedOrderByDueDateAsc(Long userId, Boolean completed);

    List<Reminder> findByUserIdAndPetIdOrderByDueDateAsc(Long userId, Long petId);

    List<Reminder> findByUserIdAndCompletedFalseAndDueDateBetweenOrderByDueDateAsc(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<Reminder> findByIdAndUserId(Long id, Long userId);
}
