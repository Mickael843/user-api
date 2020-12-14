package com.mikaeru.user_api.domain.service.email.serviceImpl;

import com.mikaeru.user_api.domain.handler.Problem;
import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.email.SendEmailService;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Properties;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired private UserService userService;

    @Autowired private UserRepository userRepository;

    private static final String USERNAME = "mickaelptu321@gmail.com";
    private static final String PASSWORD = "mickaelluiz321";

    private static final String USER_NOT_FOUND = "Usuário não encontrado!";

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public Problem sendRecoveryEmail(String username) {

        String message = "A sua nova senha de acesso é: ";
        String subject = "Recuperação da senha";

        Problem problem = new Problem();

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        String newPassword = generatePassword();

        userService.updatePassword(encoder.encode(newPassword), user.get().getId());

        message = message + newPassword;

        try {
            sendEmail(subject, user.get().getEmail(), message);
        } catch (MessagingException e) {
            try {
                throw new MessagingException("Email informado não existe!");
            } catch (MessagingException messagingException) {
                messagingException.printStackTrace();
            }
        }

        problem.setStatus(HttpStatus.OK.value());
        problem.setTitle("Senha de acesso enviado para seu email!");

        return problem;
    }

    private void sendEmail(String subject, String recipientEmail, String message) throws MessagingException {

        Properties properties = new Properties();

        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.auth", "true"); // Authorization
        properties.put("mail.smtp.starttls", "true"); // Authentication
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Google Server
        properties.put("mail.smtp.port", "465"); //Server port
        properties.put("mail.smtp.socketFactory.port", "465"); // Port socket specification
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // Connection socket class

        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Address[] toUser = InternetAddress.parse(recipientEmail);

        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(USERNAME)); // Quem está mandando o email
        msg.setRecipients(Message.RecipientType.TO, toUser); // Para quem o e-mail será enviado
        msg.setSubject(subject); // Assunto do email
        msg.setText(message);

        Transport.send(msg);
    }

    private String generatePassword() {

        int maxCharacters = 8;

        String[] characters = {
                "0", "1", "b", "2", "4", "5", "6", "7", "8",
                "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
                "x", "y", "z"
        };

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < maxCharacters; i++) {

            int index = (int) (Math.random() * characters.length);
            password.append(characters[index]);
        }

        return password.toString();
    }
}
