package com.studybuddy.studybuddy.dto;

import lombok.Data;

@Data
public class TopicResponseDTO {
    private Long id;
    private String title;
    private Integer estimatedHours;
    private Boolean completed;
    private Long subjectId;
    private String subjectName;
}
