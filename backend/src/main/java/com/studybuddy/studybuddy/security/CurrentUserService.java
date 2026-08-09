package com.studybuddy.studybuddy.security;

import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser(){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
