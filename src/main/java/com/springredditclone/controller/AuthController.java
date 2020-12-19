package com.springredditclone.controller;

import com.springredditclone.dto.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    //DTO Class
    @PostMapping("/signup")
    public void signUp(@RequestBody RegisterRequest registerRequest){

    }


}
