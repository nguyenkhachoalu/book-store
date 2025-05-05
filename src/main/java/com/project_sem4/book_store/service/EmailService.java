package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.handle.handle_email.EmailMessage;

public interface EmailService {
    String sendEmail(EmailMessage emailMessage);
    String generateConfirmationCodeEmail(String confirmationCode);
    String generateForgotPassword(String newPassword);
    String generateOrderConfirmEmail(String userName);
    String generateDeliveryCompletionEmail(String projectName);
}