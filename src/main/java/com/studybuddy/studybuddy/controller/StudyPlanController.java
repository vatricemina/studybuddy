package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.StudyPlanEntryRequestDTO;
import com.studybuddy.studybuddy.dto.StudyPlanEntryResponseDTO;
import com.studybuddy.studybuddy.dto.StudySessionRequestDTO;
import com.studybuddy.studybuddy.entity.StudyPlanEntry;
import com.studybuddy.studybuddy.service.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-plan")
public class StudyPlanController {
    @Autowired
    private StudyPlanService studyPlanService;

    @PostMapping("/generate")
    public List<StudyPlanEntryResponseDTO> generatePlan(@RequestBody StudyPlanEntryRequestDTO requestDTO) throws Exception{
        return studyPlanService.generatePlan(requestDTO.getSubjectId());
    }

    @GetMapping("/{subjectId}")
    public List<StudyPlanEntryResponseDTO> getPlan(@PathVariable Long subjectId){
        return studyPlanService.getPlanForSubject(subjectId);
    }
}
