package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
}