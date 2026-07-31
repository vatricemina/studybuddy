package com.studybuddy.studybuddy.dto;

import com.studybuddy.studybuddy.entity.SessionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudySessionResponseDTO {
    private Long id;
    private Integer plannedDurationMinutes;
    private Integer actualDurationMinutes;
    private Integer studyIntervalMinutes;
    private Integer breakIntervalMinutes;
    private Integer cyclesCompleted;
    private SessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Long topicId;
    private String topicTitle;
}
