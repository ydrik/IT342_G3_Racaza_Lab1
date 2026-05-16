package com.annimemo.service;

import com.annimemo.dto.ReminderRequest;
import com.annimemo.dto.ReminderResponse;
import com.annimemo.model.Pet;
import com.annimemo.model.Reminder;
import com.annimemo.model.User;
import com.annimemo.repository.PetRepository;
import com.annimemo.repository.ReminderRepository;
import com.annimemo.repository.UserRepository;
import com.annimemo.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reminder service for reminder CRUD and due-soon notifications.
 */
@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<ReminderResponse> getReminders(Authentication authentication, Long petId, Boolean completed) {
        User user = getCurrentUser(authentication);

        List<Reminder> reminders;
        if (petId != null) {
            reminders = reminderRepository.findByUserIdAndPetIdOrderByDueDateAsc(user.getId(), petId);
        } else if (completed != null) {
            reminders = reminderRepository.findByUserIdAndCompletedOrderByDueDateAsc(user.getId(), completed);
        } else {
            reminders = reminderRepository.findByUserIdOrderByDueDateAsc(user.getId());
        }

        return reminders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<ReminderResponse> getDueSoonReminders(Authentication authentication, int days) {
        User user = getCurrentUser(authentication);
        LocalDate today = LocalDate.now();
        LocalDate until = today.plusDays(Math.max(days, 0));

        List<Reminder> reminders = reminderRepository
                .findByUserIdAndCompletedFalseAndDueDateBetweenOrderByDueDateAsc(user.getId(), today, until);

        return reminders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Transactional
    public ReminderResponse createReminder(ReminderRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);

        Pet pet = petRepository.findByIdAndUserId(request.getPetId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Pet not found or access denied"));

        Reminder reminder = new Reminder();
        reminder.setUser(user);
        reminder.setPet(pet);
        reminder.setTitle(request.getTitle());
        reminder.setType(request.getType());
        reminder.setDueDate(request.getDueDate());
        reminder.setNotes(request.getNotes());
        reminder.setCompleted(false);

        Reminder saved = reminderRepository.save(reminder);
        return convertToResponse(saved);
    }

    @Transactional
    public ReminderResponse updateReminderStatus(Long reminderId, boolean completed, Authentication authentication) {
        User user = getCurrentUser(authentication);

        Reminder reminder = reminderRepository.findByIdAndUserId(reminderId, user.getId())
                .orElseThrow(() -> new RuntimeException("Reminder not found or access denied"));

        reminder.setCompleted(completed);
        Reminder updated = reminderRepository.save(reminder);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteReminder(Long reminderId, Authentication authentication) {
        User user = getCurrentUser(authentication);

        Reminder reminder = reminderRepository.findByIdAndUserId(reminderId, user.getId())
                .orElseThrow(() -> new RuntimeException("Reminder not found or access denied"));

        reminderRepository.delete(reminder);
    }

    @Transactional
    public int sendDueSoonReminderDigest(Authentication authentication, int days) {
        User user = getCurrentUser(authentication);
        List<ReminderResponse> dueSoon = getDueSoonReminders(authentication, days);

        if (dueSoon.isEmpty()) {
            return 0;
        }

        List<String> lines = dueSoon.stream()
                .map(r -> String.format("%s - %s (%s)", r.getDueDate(), r.getTitle(), r.getPetName()))
                .collect(Collectors.toList());

        emailService.sendReminderDigestEmail(user.getEmail(), user.getFirstName(), lines, days);
        return dueSoon.size();
    }

    private User getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ReminderResponse convertToResponse(Reminder reminder) {
        ReminderResponse response = new ReminderResponse();
        response.setId(reminder.getId());
        response.setPetId(reminder.getPet().getId());
        response.setPetName(reminder.getPet().getName());
        response.setTitle(reminder.getTitle());
        response.setType(reminder.getType());
        response.setDueDate(reminder.getDueDate());
        response.setNotes(reminder.getNotes());
        response.setCompleted(reminder.getCompleted());
        response.setCreatedAt(reminder.getCreatedAt());
        return response;
    }
}
