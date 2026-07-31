package com.studybuddy.studybuddy.dto;

import lombok.Data;

@Data
public class FlashcardResponseDTO {
    private Long id;
    private String question;
    private String answer;
    private Long topicId;
    private String topicTitle;
}