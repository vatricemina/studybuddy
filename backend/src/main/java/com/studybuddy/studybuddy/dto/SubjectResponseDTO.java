package com.studybuddy.studybuddy.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SubjectResponseDTO {
    private Long id;
    private String name;
    private LocalDate examDate;
    private Integer difficulty;
}