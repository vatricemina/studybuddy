package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.entity.Quiz;
import com.studybuddy.studybuddy.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping
    public List<Quiz> getAllQuizzes(){return quizRepository.findAll();}

    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz){return quizRepository.save(quiz);}

    @PutMapping("/{id}")
    public Quiz updateQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuiz){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + id));

        quiz.setGeneratedAt(updatedQuiz.getGeneratedAt());
        quiz.setScore(updatedQuiz.getScore());
        quiz.setTopic(updatedQuiz.getTopic());

        return quizRepository.save(quiz);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id){
        quizRepository.deleteById(id);
    }
}