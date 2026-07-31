package com.studybuddy.studybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;
    private String correctAnswer;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}