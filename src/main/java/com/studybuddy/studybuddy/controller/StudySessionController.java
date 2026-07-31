package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.StudySessionRequestDTO;
import com.studybuddy.studybuddy.dto.StudySessionResponseDTO;
import com.studybuddy.studybuddy.service.StudySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-sessions")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @GetMapping
    public List<StudySessionResponseDTO> getAllStudySessions(){
        return studySessionService.getAllStudySessions();
    }

    @PostMapping
    public StudySessionResponseDTO createStudySession(@RequestBody StudySessionRequestDTO requestDTO){
        return studySessionService.createStudySession(requestDTO);
    }

    @PutMapping("/{id}")
    public StudySessionResponseDTO updateStudySession(@PathVariable Long id, @RequestBody StudySessionRequestDTO requestDTO){
        return studySessionService.updateStudySession(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteStudySession(@PathVariable Long id){
        studySessionService.deleteStudySession(id);
    }
}