package com.springredditclone.service;

import com.springredditclone.dto.RegisterRequest;
import com.springredditclone.exceptions.SpringRedditException;
import com.springredditclone.model.NotificationEmail;
import com.springredditclone.model.User;
import com.springredditclone.model.VerificationToken;
import com.springredditclone.repository.UserRepository;
import com.springredditclone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;


    @Transactional
    public void signup(RegisterRequest registerRequest) throws SpringRedditException {
        User user = new User();
        user.setUsername((registerRequest.getUsername()));
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        //when user clicks on the URL, we take the token from the URL param, look it up in the DB, and
        //fetch the user who created the token, and enable the user
        mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(),
                "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account:" +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

   private String generateVerificationToken(User user){
       String token = UUID.randomUUID().toString();
       VerificationToken verificationToken = new VerificationToken();
       verificationToken.setToken(token);
       verificationToken.setUser(user);

       verificationTokenRepository.save(verificationToken);
       return token;

    }




}
