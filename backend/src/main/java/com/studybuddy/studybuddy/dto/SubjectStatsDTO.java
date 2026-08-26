package com.studybuddy.studybuddy.dto;

import lombok.Data;

@Data
public class SubjectStatsDTO {
    private String subjectName;
    private Integer totalStudyMinutes;
    private Double averageQuizScore;
}