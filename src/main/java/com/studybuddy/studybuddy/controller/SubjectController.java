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

    @PutMapping("/{id}")
    public Subject updateSubject(@PathVariable Long id, @RequestBody Subject updatedSubject){
        Subject subject=subjectRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Subject not found with id "+id));
        subject.setName(updatedSubject.getName());
        subject.setExamDate(updatedSubject.getExamDate());
        subject.setDifficulty(updatedSubject.getDifficulty());

        return subjectRepository.save(subject);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id){
        subjectRepository.deleteById(id);
    }
}
