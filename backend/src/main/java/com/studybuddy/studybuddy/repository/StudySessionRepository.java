package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    List<StudySession> findByTopicSubjectUserId(Long userId);

}
