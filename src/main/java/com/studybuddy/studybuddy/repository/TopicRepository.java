package com.studybuddy.studybuddy.repository;

import com.studybuddy.studybuddy.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,Long> {
}
