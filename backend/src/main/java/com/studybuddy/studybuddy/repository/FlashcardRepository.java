package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.Flashcard;
import com.studybuddy.studybuddy.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByTopicSubjectUserId(Long userId);
    List<Flashcard> findByTopicId(Long topicId);

}