package com.fernandoschimidt.paymentsystem.service;

import com.fernandoschimidt.paymentsystem.dto.UserResponse;
import com.fernandoschimidt.paymentsystem.entity.User;
import com.fernandoschimidt.paymentsystem.respository.UserRepository;
import com.fernandoschimidt.paymentsystem.service.email.EmailTemplateName;
import com.fernandoschimidt.paymentsystem.service.email.MailService;
import com.fernandoschimidt.paymentsystem.util.RandomString;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private String activationUrl = "http://localhost:8080/user/verify?code=";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    public UserResponse registerUser(User user) throws MessagingException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("This email already exists");
        } else {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            String ramdomCode = RandomString.generateRandomString(64);
            user.setVerificationCode(ramdomCode);
            user.setEnabled(false);

            User savedUser = userRepository.save(user);
            UserResponse userResponse = new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());


            //send email validation
            sendValidationEmail(user);
            return userResponse;
        }
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByverificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = user.getVerificationCode();

        mailService.sendEmail(
                user.getEmail(),
                user.getName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }
}
