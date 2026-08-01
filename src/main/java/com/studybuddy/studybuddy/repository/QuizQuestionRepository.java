package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByQuizTopicSubjectUserId(Long userId);

}