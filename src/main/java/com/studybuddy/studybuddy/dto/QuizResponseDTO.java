package com.studybuddy.studybuddy.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuizResponseDTO {
    private Long id;
    private LocalDateTime generatedAt;
    private Integer score;
    private Long topicId;
    private String topicTitle;
}