package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}