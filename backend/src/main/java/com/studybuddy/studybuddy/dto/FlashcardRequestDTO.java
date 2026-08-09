package com.studybuddy.studybuddy.dto;

import lombok.Data;

@Data
public class FlashcardRequestDTO {
    private String question;
    private String answer;
    private Long topicId;
}