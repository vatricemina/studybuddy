package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByTopicSubjectUserId(Long userId);
    List<Quiz> findByTopicSubjectId(Long subjectId);
    List<Quiz> findByTopicId(Long topicId);
}