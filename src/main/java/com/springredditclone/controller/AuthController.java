package com.springredditclone.controller;

import com.springredditclone.dto.RegisterRequest;
import com.springredditclone.exceptions.SpringRedditException;
import com.springredditclone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    //DTO Class
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) throws SpringRedditException {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                HttpStatus.OK);
    }


}
