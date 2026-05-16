package com.annimemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Email Service
 * FRS Feature 4.6: Email Sending (SMTP)
 * Sends emails for account-related and system notifications
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    /**
     * Send welcome email to new users
     * FRS Feature 4.6: Account-related email
     */
    @Async
    public void sendWelcomeEmail(String toEmail, String username, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to AnniMemo!");
            message.setText(String.format(
                    "Dear %s,\n\n" +
                    "Welcome to AnniMemo - Your Pet Health Management System!\n\n" +
                    "Your account has been successfully created with username: %s\n\n" +
                    "You can now:\n" +
                    "- Add and manage your pets\n" +
                    "- Track health metrics\n" +
                    "- Monitor vaccinations and medications\n" +
                    "- Access your pet's information anytime, anywhere\n\n" +
                    "Thank you for choosing AnniMemo!\n\n" +
                    "Best regards,\n" +
                    "The AnniMemo Team",
                    firstName, username
            ));

            mailSender.send(message);
            System.out.println("Welcome email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
        }
    }

    /**
     * Send pet health alert notification
     * FRS Feature 4.6: System notification email
     */
    @Async
    public void sendHealthAlertEmail(String toEmail, String petName, String metric, String value) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Health Alert: " + petName);
            message.setText(String.format(
                    "Dear Pet Owner,\n\n" +
                    "This is a notification regarding your pet: %s\n\n" +
                    "Health Metric: %s\n" +
                    "Recorded Value: %s\n\n" +
                    "A new health metric has been recorded for your pet. " +
                    "Please login to AnniMemo to view complete details.\n\n" +
                    "Best regards,\n" +
                    "The AnniMemo Team",
                    petName, metric, value
            ));

            mailSender.send(message);
            System.out.println("Health alert email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending health alert email: " + e.getMessage());
        }
    }

    /**
     * Send password change confirmation
     * FRS Feature 4.6: Account-related email
     */
    @Async
    public void sendPasswordChangeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Changed Successfully");
            message.setText(String.format(
                    "Dear User,\n\n" +
                    "Your password for account %s has been successfully changed.\n\n" +
                    "If you did not make this change, please contact our support immediately.\n\n" +
                    "Best regards,\n" +
                    "The AnniMemo Team",
                    username
            ));

            mailSender.send(message);
            System.out.println("Password change email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending password change email: " + e.getMessage());
        }
    }

    /**
     * Send pet added notification
     * FRS Feature 4.6: System notification email
     */
    @Async
    public void sendPetAddedEmail(String toEmail, String petName, String species) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("New Pet Added: " + petName);
            message.setText(String.format(
                    "Dear Pet Owner,\n\n" +
                    "You have successfully added a new pet to your AnniMemo account!\n\n" +
                    "Pet Name: %s\n" +
                    "Species: %s\n\n" +
                    "You can now track health metrics, vaccinations, and more for %s.\n\n" +
                    "Best regards,\n" +
                    "The AnniMemo Team",
                    petName, species, petName
            ));

            mailSender.send(message);
            System.out.println("Pet added email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending pet added email: " + e.getMessage());
        }
    }

    /**
     * Send due-soon reminder digest.
     */
    @Async
    public void sendReminderDigestEmail(String toEmail, String firstName, List<String> reminderLines, int daysWindow) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Upcoming Pet Reminders");

            StringBuilder content = new StringBuilder();
            content.append("Dear ").append(firstName).append(",\n\n");
            content.append("Here are your pet reminders due in the next ")
                    .append(daysWindow)
                    .append(" day(s):\n\n");

            for (String line : reminderLines) {
                content.append("- ").append(line).append("\n");
            }

            content.append("\nPlease login to AnniMemo for full details.\n\n");
            content.append("Best regards,\nThe AnniMemo Team");

            message.setText(content.toString());
            mailSender.send(message);
            System.out.println("Reminder digest email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending reminder digest email: " + e.getMessage());
        }
    }
}
