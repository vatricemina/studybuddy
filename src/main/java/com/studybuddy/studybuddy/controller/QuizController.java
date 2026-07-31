package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.QuizRequestDTO;
import com.studybuddy.studybuddy.dto.QuizResponseDTO;
import com.studybuddy.studybuddy.entity.Quiz;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.repository.QuizRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping
    public List<QuizResponseDTO> getAllQuizzes(){
        return quizRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public QuizResponseDTO createQuiz(@RequestBody QuizRequestDTO requestDTO){
        return toResponseDTO(quizRepository.save(toEntity(requestDTO)));
    }

    @PutMapping("/{id}")
    public QuizResponseDTO updateQuiz(@PathVariable Long id, @RequestBody QuizRequestDTO requestDTO){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + id));

        quiz.setGeneratedAt(requestDTO.getGeneratedAt());
        quiz.setScore(requestDTO.getScore());

        Topic topic = topicRepository.findById(requestDTO.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + requestDTO.getTopicId()));
        quiz.setTopic(topic);

        return toResponseDTO(quizRepository.save(quiz));
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id){
        quizRepository.deleteById(id);
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

    private Quiz toEntity(QuizRequestDTO dto){
        Quiz quiz = new Quiz();
        quiz.setGeneratedAt(dto.getGeneratedAt());
        quiz.setScore(dto.getScore());

        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + dto.getTopicId()));
        quiz.setTopic(topic);

        return quiz;
    }
}