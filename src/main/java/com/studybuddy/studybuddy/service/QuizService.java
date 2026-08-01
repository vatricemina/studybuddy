package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.QuizRequestDTO;
import com.studybuddy.studybuddy.dto.QuizResponseDTO;
import com.studybuddy.studybuddy.entity.Quiz;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.QuizRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import com.studybuddy.studybuddy.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public List<QuizResponseDTO> getAllQuizzes(){
        User currentUser = currentUserService.getCurrentUser();
        return quizRepository.findByTopicSubjectUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public QuizResponseDTO createQuiz(QuizRequestDTO requestDTO){
        Quiz quiz = toEntity(requestDTO);
        return toResponseDTO(quizRepository.save(quiz));
    }

    public QuizResponseDTO updateQuiz(Long id, QuizRequestDTO requestDTO){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + id));

        checkOwnership(quiz);

        quiz.setGeneratedAt(requestDTO.getGeneratedAt());
        quiz.setScore(requestDTO.getScore());
        quiz.setTopic(getOwnedTopic(requestDTO.getTopicId()));

        return toResponseDTO(quizRepository.save(quiz));
    }

    public void deleteQuiz(Long id){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + id));

        checkOwnership(quiz);

        quizRepository.deleteById(id);
    }

    private Topic getOwnedTopic(Long topicId){
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + topicId));

        User currentUser = currentUserService.getCurrentUser();
        if (!topic.getSubject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't own this topic");
        }
        return topic;
    }

    private void checkOwnership(Quiz quiz){
        User currentUser = currentUserService.getCurrentUser();
        if (!quiz.getTopic().getSubject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to modify this quiz");
        }
    }

    private Quiz toEntity(QuizRequestDTO dto){
        Topic topic = getOwnedTopic(dto.getTopicId());

        Quiz quiz = new Quiz();
        quiz.setGeneratedAt(dto.getGeneratedAt());
        quiz.setScore(dto.getScore());
        quiz.setTopic(topic);

        return quiz;
    }

    private QuizResponseDTO toResponseDTO(Quiz quiz){
        QuizResponseDTO dto = new QuizResponseDTO();
        dto.setId(quiz.getId());
        dto.setGeneratedAt(quiz.getGeneratedAt());
        dto.setScore(quiz.getScore());
        dto.setTopicId(quiz.getTopic().getId());
        dto.setTopicTitle(quiz.getTopic().getTitle());
        return dto;
    }
}