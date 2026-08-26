package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.SubjectRequestDTO;
import com.studybuddy.studybuddy.dto.SubjectResponseDTO;
import com.studybuddy.studybuddy.dto.SubjectStatsDTO;
import com.studybuddy.studybuddy.entity.Quiz;
import com.studybuddy.studybuddy.entity.StudySession;
import com.studybuddy.studybuddy.entity.Subject;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.QuizRepository;
import com.studybuddy.studybuddy.repository.StudySessionRepository;
import com.studybuddy.studybuddy.repository.SubjectRepository;
import com.studybuddy.studybuddy.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<SubjectResponseDTO> getAllSubjects(){
        User currentUser=currentUserService.getCurrentUser();
        return subjectRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SubjectResponseDTO createSubject(SubjectRequestDTO requestDTO){
        User currentUser=currentUserService.getCurrentUser();

        Subject subject = toEntity(requestDTO);
        subject.setUser(currentUser);
        Subject saved = subjectRepository.save(subject);
        return toResponseDTO(saved);
    }

    public SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO requestDTO){
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + id));
        User currentUser=currentUserService.getCurrentUser();
        if(!subject.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to modify this subject");
        }

        subject.setName(requestDTO.getName());
        subject.setExamDate(requestDTO.getExamDate());
        subject.setDifficulty(requestDTO.getDifficulty());


        Subject saved = subjectRepository.save(subject);
        return toResponseDTO(saved);
    }

    public void deleteSubject(Long id){
        Subject subject=subjectRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Subject not found with id " + id));

        User currentUser=currentUserService.getCurrentUser();
        if(!subject.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to delete this subject");
        }

        subjectRepository.deleteById(id);
    }

    public SubjectStatsDTO getSubjectStats(Long subjectId) {
        Subject subject = getOwnedSubject(subjectId);

        List<StudySession> sessions = studySessionRepository.findByTopicSubjectId(subjectId);
        int totalMinutes = sessions.stream()
                .filter(s -> s.getActualDurationMinutes() != null)
                .mapToInt(StudySession::getActualDurationMinutes)
                .sum();

        List<Quiz> quizzes = quizRepository.findByTopicSubjectId(subjectId);
        List<Integer> scores = quizzes.stream()
                .filter(q -> q.getScore() != null)
                .map(Quiz::getScore)
                .collect(Collectors.toList());

        Double avgScore = scores.isEmpty() ? null :
                scores.stream().mapToInt(Integer::intValue).average().orElse(0);

        SubjectStatsDTO dto = new SubjectStatsDTO();
        dto.setSubjectName(subject.getName());
        dto.setTotalStudyMinutes(totalMinutes);
        dto.setAverageQuizScore(avgScore);
        return dto;
    }

    private SubjectResponseDTO toResponseDTO(Subject subject){
        SubjectResponseDTO dto = new SubjectResponseDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setExamDate(subject.getExamDate());
        dto.setDifficulty(subject.getDifficulty());
        return dto;
    }

    private Subject getOwnedSubject(Long subjectId){
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + subjectId));

        User currentUser = currentUserService.getCurrentUser();
        if (!subject.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't own this subject");
        }
        return subject;
    }

    private Subject toEntity(SubjectRequestDTO dto){
        Subject subject = new Subject();
        subject.setName(dto.getName());
        subject.setExamDate(dto.getExamDate());
        subject.setDifficulty(dto.getDifficulty());
        return subject;
    }
}