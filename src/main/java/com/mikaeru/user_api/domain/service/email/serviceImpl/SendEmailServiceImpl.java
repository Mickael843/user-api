package com.mikaeru.user_api.domain.service.email.serviceImpl;

import com.mikaeru.user_api.domain.service.email.SendEmailService;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    private static final String USERNAME = "mickaelptu321@gmail.com";
    private static final String PASSWORD = "mickaelluiz321";

    @Override
    public void sendEmail(String subject, String recipientEmail, String message) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.auth", "true"); //Authorization
        properties.put("mail.smtp.starttls", "true"); //Authentication
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Google Server
        properties.put("mail.smtp.port", "465"); //Server port
        properties.put("mail.smtp.socketFactory.port", "465"); // Port socket specification
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //Connection socket class

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Address[] toUser = InternetAddress.parse(recipientEmail);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(USERNAME)); //Who is sending the email
        msg.setRecipients(Message.RecipientType.TO, toUser); //Who the email will be sent to
        msg.setSubject(subject); //Email subject
        msg.setText(message);

        Transport.send(msg);
    }
}
