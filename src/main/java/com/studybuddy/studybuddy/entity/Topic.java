package com.studybuddy.studybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Topic {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer estimatedHours;
    private Boolean completed;

    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;
}
