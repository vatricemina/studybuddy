package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.AuthResponseDTO;
import com.studybuddy.studybuddy.dto.LoginRequestDTO;
import com.studybuddy.studybuddy.dto.RegisterRequestDTO;
import com.studybuddy.studybuddy.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,String>> handleException(RuntimeException e){
        Map<String,String> error=new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
