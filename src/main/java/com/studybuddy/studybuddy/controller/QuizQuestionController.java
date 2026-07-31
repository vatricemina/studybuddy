package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.entity.QuizQuestion;
import com.studybuddy.studybuddy.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-questions")
public class QuizQuestionController {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @GetMapping
    public List<QuizQuestion> getAllQuizQuestions(){return quizQuestionRepository.findAll();}

    @PostMapping
    public QuizQuestion createQuizQuestion(@RequestBody QuizQuestion quizQuestion){return quizQuestionRepository.save(quizQuestion);}

    @PutMapping("/{id}")
    public QuizQuestion updateQuizQuestion(@PathVariable Long id, @RequestBody QuizQuestion updatedQuizQuestion){
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizQuestion not found with id " + id));

        quizQuestion.setQuestionText(updatedQuizQuestion.getQuestionText());
        quizQuestion.setCorrectAnswer(updatedQuizQuestion.getCorrectAnswer());
        quizQuestion.setOptionA(updatedQuizQuestion.getOptionA());
        quizQuestion.setOptionB(updatedQuizQuestion.getOptionB());
        quizQuestion.setOptionC(updatedQuizQuestion.getOptionC());
        quizQuestion.setOptionD(updatedQuizQuestion.getOptionD());
        quizQuestion.setQuiz(updatedQuizQuestion.getQuiz());

        return quizQuestionRepository.save(quizQuestion);
    }

    @DeleteMapping("/{id}")
    public void deleteQuizQuestion(@PathVariable Long id){
        quizQuestionRepository.deleteById(id);
    }
}