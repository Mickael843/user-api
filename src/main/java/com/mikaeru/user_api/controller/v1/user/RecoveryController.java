package com.mikaeru.user_api.controller.v1.user;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.email.SendEmailService;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.exceptionHandler.Problem;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/recovery", produces = "application/json")
public class RecoveryController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SendEmailService emailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Problem> recovery(@RequestBody User user) throws MessagingException {
        Problem problem = new Problem();
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

        if (userOptional.isEmpty()) {
            problem.setStatus(HttpStatus.NOT_FOUND.value());
            problem.setTitle("User not found");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String newPassword = dateFormat.format(Calendar.getInstance().getTime());
            userService.updatePassword(encoder.encode(newPassword), userOptional.get().getId());

            emailService.sendEmail(
                    "Recuperação de senha",
                    userOptional.get().getEmail(),
                    "Sua nova senha é: " + newPassword
            );

            problem.setStatus(HttpStatus.OK.value());
            problem.setTitle("Access sent to your email");
        }

        return ResponseEntity.ok(problem);
    }
}
