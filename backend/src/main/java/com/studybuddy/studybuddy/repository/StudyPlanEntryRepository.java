package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.StudyPlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyPlanEntryRepository extends JpaRepository<StudyPlanEntry, Long> {
    List<StudyPlanEntry> findByTopicSubjectUserId(Long userId);
    List<StudyPlanEntry> findByTopicSubjectId(Long subjectId);
    List<StudyPlanEntry> findByTopicId(Long topicId);

}
