package com.springredditclone.service;

import com.springredditclone.dto.AuthenticationResponse;
import com.springredditclone.dto.LoginRequest;
import com.springredditclone.dto.RefreshTokenRequest;
import com.springredditclone.dto.RegisterRequest;
import com.springredditclone.exceptions.SpringRedditException;
import com.springredditclone.model.NotificationEmail;
import com.springredditclone.model.RefreshToken;
import com.springredditclone.model.User;
import com.springredditclone.model.VerificationToken;
import com.springredditclone.repository.UserRepository;
import com.springredditclone.repository.VerificationTokenRepository;
import com.springredditclone.security.JwtProvider;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.springredditclone.Util.Constants.ACTIVATION_EMAIL;
import static java.time.Instant.now;

@AllArgsConstructor
@Service
@Slf4j
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws SpringRedditException {
        User user = new User();
        user.setUsername((registerRequest.getUsername()));
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(now());
        user.setEnabled(false);

        userRepository.save(user);

        log.info("User Registered Successfully, Sending Authentication Email");

        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build("Thank you for singing up to Spring Reddit, please click on the below url " +
                "to activate your account : "
        + ACTIVATION_EMAIL + "/" + token);
        //when user clicks on the URL, we take the token from the URL param, look it up in the DB, and
        //fetch the user who created the token, and enable the user
        mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(),
                message));
    }

   private String generateVerificationToken(User user){
       String token = UUID.randomUUID().toString();
       VerificationToken verificationToken = new VerificationToken();
       verificationToken.setToken(token);
       verificationToken.setUser(user);

       verificationTokenRepository.save(verificationToken);
       return token;
    }

    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws SpringRedditException {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
       return AuthenticationResponse.builder()
               .authenticationToken(token)
               .refreshToken(refreshTokenService.generateRefreshToken().getToken())
               .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
               .username(loginRequest.getUsername())
               .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws SpringRedditException {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
            return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())

                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                        .username(refreshTokenRequest.getUsername())
                        .build();
    }

    public void verifyAccount(String token) throws SpringRedditException {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringRedditException("invalid Token")));
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) throws SpringRedditException {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User " + "Not Found with id - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional(readOnly =  true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found - " + principal.getUsername()));
    }
}
