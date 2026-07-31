package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.AuthResponseDTO;
import com.studybuddy.studybuddy.dto.LoginRequestDTO;
import com.studybuddy.studybuddy.dto.RegisterRequestDTO;
import com.studybuddy.studybuddy.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody RegisterRequestDTO requestDTO){
        return authService.register(requestDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO requestDTO){
        return authService.login(requestDTO);
    }
}
