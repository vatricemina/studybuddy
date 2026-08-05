package com.studybuddy.studybuddy.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequestDTO {
    private Long topicId;
    private List<ChatMessageDTO> messages;
}
