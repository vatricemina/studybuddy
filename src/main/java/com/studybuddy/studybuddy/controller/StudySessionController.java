package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.entity.StudySession;
import com.studybuddy.studybuddy.repository.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-sessions")
public class StudySessionController {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @GetMapping
    public List<StudySession> getAllStudySessions(){return studySessionRepository.findAll();}

    @PostMapping
    public StudySession createStudySession(@RequestBody StudySession studySession){return studySessionRepository.save(studySession);}

    @PutMapping("/{id}")
    public StudySession updateStudySession(@PathVariable Long id, @RequestBody StudySession updatedStudySession){
        StudySession studySession=studySessionRepository.findById(id)
                .orElseThrow(()->new RuntimeException("StudySession not found by id "+id));

        studySession.setPlannedDurationMinutes(updatedStudySession.getPlannedDurationMinutes());
        studySession.setActualDurationMinutes(updatedStudySession.getActualDurationMinutes());
        studySession.setStudyIntervalMinutes(updatedStudySession.getStudyIntervalMinutes());
        studySession.setBreakIntervalMinutes(updatedStudySession.getBreakIntervalMinutes());
        studySession.setCyclesCompleted(updatedStudySession.getCyclesCompleted());
        studySession.setStatus(updatedStudySession.getStatus());
        studySession.setStartedAt(updatedStudySession.getStartedAt());
        studySession.setEndedAt(updatedStudySession.getEndedAt());
        studySession.setTopic(updatedStudySession.getTopic());


        return studySessionRepository.save(studySession);
    }

    @DeleteMapping("/{id}")
    public void deleteStudySession(@PathVariable Long id){
        studySessionRepository.deleteById(id);
    }
}
