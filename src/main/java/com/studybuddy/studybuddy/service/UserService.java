package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.UserRequestDTO;
import com.studybuddy.studybuddy.dto.UserResponseDTO;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setEmail(requestDTO.getEmail());

        return toResponseDTO(userRepository.save(user));
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    private UserResponseDTO toResponseDTO(User user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}