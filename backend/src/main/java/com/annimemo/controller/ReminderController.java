package com.annimemo.controller;

import com.annimemo.dto.MessageResponse;
import com.annimemo.dto.ReminderRequest;
import com.annimemo.dto.ReminderResponse;
import com.annimemo.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Reminder controller for creating and managing reminders.
 */
@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping
    public ResponseEntity<?> getReminders(
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) Boolean completed,
            Authentication authentication
    ) {
        try {
            List<ReminderResponse> reminders = reminderService.getReminders(authentication, petId, completed);
            return ResponseEntity.ok(reminders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/due-soon")
    public ResponseEntity<?> getDueSoonReminders(
            @RequestParam(defaultValue = "7") int days,
            Authentication authentication
    ) {
        try {
            List<ReminderResponse> reminders = reminderService.getDueSoonReminders(authentication, days);
            return ResponseEntity.ok(reminders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createReminder(
            @Valid @RequestBody ReminderRequest request,
            Authentication authentication
    ) {
        try {
            ReminderResponse reminder = reminderService.createReminder(request, authentication);
            return ResponseEntity.ok(reminder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateReminderStatus(
            @PathVariable Long id,
            @RequestParam boolean completed,
            Authentication authentication
    ) {
        try {
            ReminderResponse reminder = reminderService.updateReminderStatus(id, completed, authentication);
            return ResponseEntity.ok(reminder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable Long id, Authentication authentication) {
        try {
            reminderService.deleteReminder(id, authentication);
            return ResponseEntity.ok(new MessageResponse("Reminder deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/notify-due-soon")
    public ResponseEntity<?> notifyDueSoon(
            @RequestParam(defaultValue = "7") int days,
            Authentication authentication
    ) {
        try {
            int sentCount = reminderService.sendDueSoonReminderDigest(authentication, days);
            return ResponseEntity.ok(new MessageResponse("Reminder digest sent for " + sentCount + " reminder(s)"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
