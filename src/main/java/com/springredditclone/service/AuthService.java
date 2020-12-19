package com.springredditclone.service;

import com.springredditclone.dto.RegisterRequest;
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


    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername((registerRequest.getUsername()));
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        Sring token = generateVerificationToken(user);
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
