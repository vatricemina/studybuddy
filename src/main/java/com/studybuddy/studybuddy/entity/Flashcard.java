package com.studybuddy.studybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}