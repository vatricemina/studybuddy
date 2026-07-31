package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
}