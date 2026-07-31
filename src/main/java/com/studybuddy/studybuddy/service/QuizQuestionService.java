package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.QuizQuestionRequestDTO;
import com.studybuddy.studybuddy.dto.QuizQuestionResponseDTO;
import com.studybuddy.studybuddy.entity.Quiz;
import com.studybuddy.studybuddy.entity.QuizQuestion;
import com.studybuddy.studybuddy.repository.QuizQuestionRepository;
import com.studybuddy.studybuddy.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizQuestionService {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<QuizQuestionResponseDTO> getAllQuizQuestions(){
        return quizQuestionRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public QuizQuestionResponseDTO createQuizQuestion(QuizQuestionRequestDTO requestDTO){
        return toResponseDTO(quizQuestionRepository.save(toEntity(requestDTO)));
    }

    public QuizQuestionResponseDTO updateQuizQuestion(Long id, QuizQuestionRequestDTO requestDTO){
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

    public void deleteQuizQuestion(Long id){
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