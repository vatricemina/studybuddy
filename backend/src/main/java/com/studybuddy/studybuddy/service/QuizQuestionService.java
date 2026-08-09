package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.QuizQuestionRequestDTO;
import com.studybuddy.studybuddy.dto.QuizQuestionResponseDTO;
import com.studybuddy.studybuddy.entity.Quiz;
import com.studybuddy.studybuddy.entity.QuizQuestion;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.QuizQuestionRepository;
import com.studybuddy.studybuddy.repository.QuizRepository;
import com.studybuddy.studybuddy.security.CurrentUserService;
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

    @Autowired
    private CurrentUserService currentUserService;

    public List<QuizQuestionResponseDTO> getAllQuizQuestions(){
        User currentUser = currentUserService.getCurrentUser();
        return quizQuestionRepository.findByQuizTopicSubjectUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public QuizQuestionResponseDTO createQuizQuestion(QuizQuestionRequestDTO requestDTO){
        QuizQuestion quizQuestion = toEntity(requestDTO);
        return toResponseDTO(quizQuestionRepository.save(quizQuestion));
    }

    public QuizQuestionResponseDTO updateQuizQuestion(Long id, QuizQuestionRequestDTO requestDTO){
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizQuestion not found with id " + id));

        checkOwnership(quizQuestion);

        quizQuestion.setQuestionText(requestDTO.getQuestionText());
        quizQuestion.setCorrectAnswer(requestDTO.getCorrectAnswer());
        quizQuestion.setOptionA(requestDTO.getOptionA());
        quizQuestion.setOptionB(requestDTO.getOptionB());
        quizQuestion.setOptionC(requestDTO.getOptionC());
        quizQuestion.setOptionD(requestDTO.getOptionD());
        quizQuestion.setQuiz(getOwnedQuiz(requestDTO.getQuizId()));

        return toResponseDTO(quizQuestionRepository.save(quizQuestion));
    }

    public void deleteQuizQuestion(Long id){
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizQuestion not found with id " + id));

        checkOwnership(quizQuestion);

        quizQuestionRepository.deleteById(id);
    }

    private Quiz getOwnedQuiz(Long quizId){
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + quizId));

        User currentUser = currentUserService.getCurrentUser();
        if (!quiz.getTopic().getSubject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't own this quiz");
        }
        return quiz;
    }

    private void checkOwnership(QuizQuestion quizQuestion){
        User currentUser = currentUserService.getCurrentUser();
        if (!quizQuestion.getQuiz().getTopic().getSubject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to modify this quiz question");
        }
    }

    private QuizQuestion toEntity(QuizQuestionRequestDTO dto){
        Quiz quiz = getOwnedQuiz(dto.getQuizId());

        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setQuestionText(dto.getQuestionText());
        quizQuestion.setCorrectAnswer(dto.getCorrectAnswer());
        quizQuestion.setOptionA(dto.getOptionA());
        quizQuestion.setOptionB(dto.getOptionB());
        quizQuestion.setOptionC(dto.getOptionC());
        quizQuestion.setOptionD(dto.getOptionD());
        quizQuestion.setQuiz(quiz);

        return quizQuestion;
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
}