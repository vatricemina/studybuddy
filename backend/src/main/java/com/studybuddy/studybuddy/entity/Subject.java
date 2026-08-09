package com.studybuddy.studybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate examDate;
    private Integer difficulty;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
