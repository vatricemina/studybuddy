package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.QuizQuestionRequestDTO;
import com.studybuddy.studybuddy.dto.QuizQuestionResponseDTO;
import com.studybuddy.studybuddy.entity.Quiz;
import com.studybuddy.studybuddy.entity.QuizQuestion;
import com.studybuddy.studybuddy.repository.QuizQuestionRepository;
import com.studybuddy.studybuddy.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quiz-questions")
public class QuizQuestionController {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping
    public List<QuizQuestionResponseDTO> getAllQuizQuestions(){
        return quizQuestionRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public QuizQuestionResponseDTO createQuizQuestion(@RequestBody QuizQuestionRequestDTO requestDTO){
        return toResponseDTO(quizQuestionRepository.save(toEntity(requestDTO)));
    }

    @PutMapping("/{id}")
    public QuizQuestionResponseDTO updateQuizQuestion(@PathVariable Long id, @RequestBody QuizQuestionRequestDTO requestDTO){
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizQuestion not found with id " + id));

        quizQuestion.setQuestionText(requestDTO.getQuestionText());
        quizQuestion.setCorrectAnswer(requestDTO.getCorrectAnswer());
        quizQuestion.setOptionA(requestDTO.getOptionA());
        quizQuestion.setOptionB(requestDTO.getOptionB());
        quizQuestion.setOptionC(requestDTO.getOptionC());
        quizQuestion.setOptionD(requestDTO.getOptionD());

        Quiz quiz = quizRepository.findById(requestDTO.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + requestDTO.getQuizId()));
        quizQuestion.setQuiz(quiz);

        return toResponseDTO(quizQuestionRepository.save(quizQuestion));
    }

    @DeleteMapping("/{id}")
    public void deleteQuizQuestion(@PathVariable Long id){
        quizQuestionRepository.deleteById(id);
    }

    private QuizQuestionResponseDTO toResponseDTO(QuizQuestion quizQuestion){
        QuizQuestionResponseDTO dto = new QuizQuestionResponseDTO();
        dto.setId(quizQuestion.getId());
        dto.setQuestionText(quizQuestion.getQuestionText());
        dto.setCorrectAnswer(quizQuestion.getCorrectAnswer());
        dto.setOptionA(quizQuestion.getOptionA());
        dto.setOptionB(quizQuestion.getOptionB());
        dto.setOptionC(quizQuestion.getOptionC());
        dto.setOptionD(quizQuestion.getOptionD());
        dto.setQuizId(quizQuestion.getQuiz().getId());
        return dto;
    }

    private QuizQuestion toEntity(QuizQuestionRequestDTO dto){
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setQuestionText(dto.getQuestionText());
        quizQuestion.setCorrectAnswer(dto.getCorrectAnswer());
        quizQuestion.setOptionA(dto.getOptionA());
        quizQuestion.setOptionB(dto.getOptionB());
        quizQuestion.setOptionC(dto.getOptionC());
        quizQuestion.setOptionD(dto.getOptionD());

        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + dto.getQuizId()));
        quizQuestion.setQuiz(quiz);

        return quizQuestion;
    }
}