package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.entity.Subject;
import com.studybuddy.studybuddy.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public List<Subject> getAllSubjects(){
        return subjectRepository.findAll();
    }

    @PostMapping
    public Subject createSubject(@RequestBody Subject subject){
        return subjectRepository.save(subject);
    }
}
