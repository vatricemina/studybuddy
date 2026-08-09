package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long> {
    List<Topic> findBySubjectUserId(Long userId);
    List<Topic> findBySubjectId(Long subjectId);
}
