package com.studybuddy.studybuddy.dto;

import lombok.Data;

@Data
public class QuizQuestionAIItem {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
}
