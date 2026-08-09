package com.studybuddy.studybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class StudyPlanEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate plannedDate;
    private Integer plannedHours;

    @ManyToOne
    @JoinColumn(name="topic_id")
    private Topic topic;
    private String focus;
}
