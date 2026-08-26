package com.studybuddy.studybuddy.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmitQuizRequestDTO {
    private List<QuestionAnswerDTO> answers;

    @Data
    public static class QuestionAnswerDTO{
        private Long questionId;
        private String userAnswer;
    }
}
