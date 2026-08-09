package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.QuizQuestionRequestDTO;
import com.studybuddy.studybuddy.dto.QuizQuestionResponseDTO;
import com.studybuddy.studybuddy.service.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-questions")
public class QuizQuestionController {

    @Autowired
    private QuizQuestionService quizQuestionService;

    @GetMapping
    public List<QuizQuestionResponseDTO> getAllQuizQuestions(){
        return quizQuestionService.getAllQuizQuestions();
    }

    @PostMapping
    public QuizQuestionResponseDTO createQuizQuestion(@RequestBody QuizQuestionRequestDTO requestDTO){
        return quizQuestionService.createQuizQuestion(requestDTO);
    }

    @PutMapping("/{id}")
    public QuizQuestionResponseDTO updateQuizQuestion(@PathVariable Long id, @RequestBody QuizQuestionRequestDTO requestDTO){
        return quizQuestionService.updateQuizQuestion(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteQuizQuestion(@PathVariable Long id){
        quizQuestionService.deleteQuizQuestion(id);
    }
}