package com.studybuddy.studybuddy.dto;

import lombok.Data;

@Data
public class TopicRequestDTO {
    private String title;
    private Integer estimatedHours;
    private Boolean completed;
    private Long subjectId;
}
