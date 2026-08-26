package com.studybuddy.studybuddy.dto;

import lombok.Data;

@Data
public class QuizQuestionResponseDTO {
    private Long id;
    private String questionText;
    private String correctAnswer;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Long quizId;
    private String userAnswer;
}