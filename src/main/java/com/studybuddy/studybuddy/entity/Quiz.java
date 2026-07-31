package com.studybuddy.studybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime generatedAt;
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}