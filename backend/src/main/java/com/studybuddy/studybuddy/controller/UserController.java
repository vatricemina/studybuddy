package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.UserRequestDTO;
import com.studybuddy.studybuddy.dto.UserResponseDTO;
import com.studybuddy.studybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserResponseDTO getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserRequestDTO requestDTO){
        return userService.updateUser(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}