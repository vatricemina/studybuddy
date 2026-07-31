package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.StudySessionRequestDTO;
import com.studybuddy.studybuddy.dto.StudySessionResponseDTO;
import com.studybuddy.studybuddy.entity.StudySession;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.repository.StudySessionRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/study-sessions")
public class StudySessionController {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping
    public List<StudySessionResponseDTO> getAllStudySessions(){
        return studySessionRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public StudySessionResponseDTO createStudySession(@RequestBody StudySessionRequestDTO requestDTO){
        StudySession studySession=toEntity(requestDTO);
        return toResponseDTO(studySessionRepository.save(studySession));

    }

    @PutMapping("/{id}")
    public StudySessionResponseDTO updateStudySession(@PathVariable Long id, @RequestBody StudySessionRequestDTO requestDTO){
        StudySession studySession=studySessionRepository.findById(id)
                .orElseThrow(()->new RuntimeException("StudySession not found by id "+id));

        studySession.setPlannedDurationMinutes(requestDTO.getPlannedDurationMinutes());
        studySession.setActualDurationMinutes(requestDTO.getActualDurationMinutes());
        studySession.setStudyIntervalMinutes(requestDTO.getStudyIntervalMinutes());
        studySession.setBreakIntervalMinutes(requestDTO.getBreakIntervalMinutes());
        studySession.setCyclesCompleted(requestDTO.getCyclesCompleted());
        studySession.setStatus(requestDTO.getStatus());
        studySession.setStartedAt(requestDTO.getStartedAt());
        studySession.setEndedAt(requestDTO.getEndedAt());

        Topic topic=topicRepository.findById(requestDTO.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + requestDTO.getTopicId()));

        studySession.setTopic(topic);
        StudySession saved=studySessionRepository.save(studySession);
        return toResponseDTO(saved);

    }

    @DeleteMapping("/{id}")
    public void deleteStudySession(@PathVariable Long id){
        studySessionRepository.deleteById(id);
    }

    //pomocne
    private StudySessionResponseDTO toResponseDTO(StudySession studySession){
        StudySessionResponseDTO responseDTO=new StudySessionResponseDTO();
        responseDTO.setId(studySession.getId());
        responseDTO.setPlannedDurationMinutes(studySession.getPlannedDurationMinutes());
        responseDTO.setActualDurationMinutes(studySession.getActualDurationMinutes());
        responseDTO.setStudyIntervalMinutes(studySession.getStudyIntervalMinutes());
        responseDTO.setBreakIntervalMinutes(studySession.getBreakIntervalMinutes());
        responseDTO.setCyclesCompleted(studySession.getCyclesCompleted());
        responseDTO.setStatus(studySession.getStatus());
        responseDTO.setStartedAt(studySession.getStartedAt());
        responseDTO.setEndedAt(studySession.getEndedAt());
        responseDTO.setTopicId(studySession.getTopic().getId());
        responseDTO.setTopicTitle(studySession.getTopic().getTitle());

        return responseDTO;
    }

    private StudySession toEntity(StudySessionRequestDTO requestDTO){
        StudySession studySession=new StudySession();
        studySession.setPlannedDurationMinutes(requestDTO.getPlannedDurationMinutes());
        studySession.setActualDurationMinutes(requestDTO.getActualDurationMinutes());
        studySession.setStudyIntervalMinutes(requestDTO.getStudyIntervalMinutes());
        studySession.setBreakIntervalMinutes(requestDTO.getBreakIntervalMinutes());
        studySession.setCyclesCompleted(requestDTO.getCyclesCompleted());
        studySession.setStatus(requestDTO.getStatus());
        studySession.setStartedAt(requestDTO.getStartedAt());
        studySession.setEndedAt(requestDTO.getEndedAt());

        Topic topic=topicRepository.findById(requestDTO.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + requestDTO.getTopicId()));

        studySession.setTopic(topic);

        return studySession;
    }
}
