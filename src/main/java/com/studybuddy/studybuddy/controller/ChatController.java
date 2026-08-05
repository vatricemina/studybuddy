package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.ChatRequestDTO;
import com.studybuddy.studybuddy.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping
    public String chat(@RequestBody ChatRequestDTO requestDTO) throws Exception{
        return chatService.chat(requestDTO.getTopicId(), requestDTO.getMessages());
    }
}
