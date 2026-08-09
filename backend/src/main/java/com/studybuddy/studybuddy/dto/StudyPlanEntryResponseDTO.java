package com.studybuddy.studybuddy.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudyPlanEntryResponseDTO {
    private Long id;
    private LocalDate plannedDate;
    private Integer plannedHours;
    private Long topicId;
    private String topicTitle;
    private String focus;
}
