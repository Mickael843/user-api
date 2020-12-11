package com.mikaeru.user_api.domain.service.email;

import javax.mail.MessagingException;

public interface SendEmailService {

    void sendEmail(String topic, String recipientEmail, String message) throws MessagingException;
}
