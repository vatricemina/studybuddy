package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.SubjectRequestDTO;
import com.studybuddy.studybuddy.dto.SubjectResponseDTO;
import com.studybuddy.studybuddy.dto.SubjectStatsDTO;
import com.studybuddy.studybuddy.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<SubjectResponseDTO> getAllSubjects(){
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}/stats")
    public SubjectStatsDTO getStats(@PathVariable Long id) {
        return subjectService.getSubjectStats(id);
    }

    @PostMapping
    public SubjectResponseDTO createSubject(@RequestBody SubjectRequestDTO requestDTO){
        return subjectService.createSubject(requestDTO);
    }

    @PutMapping("/{id}")
    public SubjectResponseDTO updateSubject(@PathVariable Long id, @RequestBody SubjectRequestDTO requestDTO){
        return subjectService.updateSubject(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id){
        subjectService.deleteSubject(id);
    }
}