package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.StudySessionRequestDTO;
import com.studybuddy.studybuddy.dto.StudySessionResponseDTO;
import com.studybuddy.studybuddy.entity.StudySession;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.StudySessionRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import com.studybuddy.studybuddy.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public List<StudySessionResponseDTO> getAllStudySessions(){
        User currentUser=currentUserService.getCurrentUser();
        return studySessionRepository.findByTopicSubjectUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public StudySessionResponseDTO createStudySession(StudySessionRequestDTO requestDTO){
        StudySession studySession = toEntity(requestDTO);
        return toResponseDTO(studySessionRepository.save(studySession));
    }

    public StudySessionResponseDTO updateStudySession(Long id, StudySessionRequestDTO requestDTO){
        StudySession studySession = studySessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudySession not found by id " + id));

        checkOwnership(studySession);
        studySession.setPlannedDurationMinutes(requestDTO.getPlannedDurationMinutes());
        studySession.setActualDurationMinutes(requestDTO.getActualDurationMinutes());
        studySession.setStudyIntervalMinutes(requestDTO.getStudyIntervalMinutes());
        studySession.setBreakIntervalMinutes(requestDTO.getBreakIntervalMinutes());
        studySession.setCyclesCompleted(requestDTO.getCyclesCompleted());
        studySession.setStatus(requestDTO.getStatus());
        studySession.setStartedAt(requestDTO.getStartedAt());
        studySession.setEndedAt(requestDTO.getEndedAt());

        studySession.setTopic(getOwnedTopic(requestDTO.getTopicId()));

        return toResponseDTO(studySessionRepository.save(studySession));
    }

    public void deleteStudySession(Long id){
        StudySession studySession=studySessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudySession not found by id " + id));
        checkOwnership(studySession);
        studySessionRepository.deleteById(id);
    }


    private Topic getOwnedTopic(Long topicId){
        Topic topic=topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + topicId));
        User currentUser=currentUserService.getCurrentUser();
        if(!topic.getSubject().getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to modify this study session");
        }
        return topic;
    }

    private void checkOwnership(StudySession studySession){
        User currentUser=currentUserService.getCurrentUser();
        if(!studySession.getTopic().getSubject().getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to modify this study session");
        }
    }

    private StudySessionResponseDTO toResponseDTO(StudySession studySession){
        StudySessionResponseDTO dto = new StudySessionResponseDTO();
        dto.setId(studySession.getId());
        dto.setPlannedDurationMinutes(studySession.getPlannedDurationMinutes());
        dto.setActualDurationMinutes(studySession.getActualDurationMinutes());
        dto.setStudyIntervalMinutes(studySession.getStudyIntervalMinutes());
        dto.setBreakIntervalMinutes(studySession.getBreakIntervalMinutes());
        dto.setCyclesCompleted(studySession.getCyclesCompleted());
        dto.setStatus(studySession.getStatus());
        dto.setStartedAt(studySession.getStartedAt());
        dto.setEndedAt(studySession.getEndedAt());
        dto.setTopicId(studySession.getTopic().getId());
        dto.setTopicTitle(studySession.getTopic().getTitle());
        return dto;
    }

    private StudySession toEntity(StudySessionRequestDTO requestDTO){
        Topic topic=getOwnedTopic(requestDTO.getTopicId());

        StudySession studySession = new StudySession();
        studySession.setPlannedDurationMinutes(requestDTO.getPlannedDurationMinutes());
        studySession.setActualDurationMinutes(requestDTO.getActualDurationMinutes());
        studySession.setStudyIntervalMinutes(requestDTO.getStudyIntervalMinutes());
        studySession.setBreakIntervalMinutes(requestDTO.getBreakIntervalMinutes());
        studySession.setCyclesCompleted(requestDTO.getCyclesCompleted());
        studySession.setStatus(requestDTO.getStatus());
        studySession.setStartedAt(requestDTO.getStartedAt());
        studySession.setEndedAt(requestDTO.getEndedAt());

        studySession.setTopic(topic);

        return studySession;
    }
}