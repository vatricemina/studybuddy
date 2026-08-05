package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.GroqQuizRequest;
import com.studybuddy.studybuddy.dto.QuizRequestDTO;
import com.studybuddy.studybuddy.dto.QuizResponseDTO;
import com.studybuddy.studybuddy.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping
    public List<QuizResponseDTO> getAllQuizzes(){
        return quizService.getAllQuizzes();
    }

    @PostMapping
    public QuizResponseDTO createQuiz(@RequestBody QuizRequestDTO requestDTO){
        return quizService.createQuiz(requestDTO);
    }

    @PostMapping("/generate")
    public QuizResponseDTO generateQuiz(@RequestBody GroqQuizRequest requestDto)throws Exception{
        return  quizService.generateQuizForTopic(requestDto.getTopicId());
    }

    @PutMapping("/{id}")
    public QuizResponseDTO updateQuiz(@PathVariable Long id, @RequestBody QuizRequestDTO requestDTO){
        return quizService.updateQuiz(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id){
        quizService.deleteQuiz(id);
    }
}