package com.studybuddy.studybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class StudySession {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Integer plannedDurationMinutes;
    private Integer actualDurationMinutes;
    private Integer studyIntervalMinutes;
    private Integer breakIntervalMinutes;
    private Integer cyclesCompleted;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @ManyToOne
    @JoinColumn(name="topic_id")
    private Topic topic;

}
