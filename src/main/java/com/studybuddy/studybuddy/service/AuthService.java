package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.AuthResponseDTO;
import com.studybuddy.studybuddy.dto.LoginRequestDTO;
import com.studybuddy.studybuddy.dto.RegisterRequestDTO;
import com.studybuddy.studybuddy.entity.Role;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.UserRepository;
import com.studybuddy.studybuddy.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponseDTO register(RegisterRequestDTO requestDTO){
        if(userRepository.findByEmail(requestDTO.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");
        }

        User user=new User();
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token=jwtUtil.generateToken(user.getEmail());
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginRequestDTO requestDTO){
        User user=userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(()->new RuntimeException("Invalid email or password"));

        if(!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }

        String token=jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDTO(token);
    }

}
