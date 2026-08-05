package com.studybuddy.studybuddy.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private String role; //user ili assistant
    private String content;
}
