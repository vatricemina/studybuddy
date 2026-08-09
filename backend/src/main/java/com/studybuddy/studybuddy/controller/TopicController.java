package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.TopicRequestDTO;
import com.studybuddy.studybuddy.dto.TopicResponseDTO;
import com.studybuddy.studybuddy.entity.StudySession;
import com.studybuddy.studybuddy.entity.Subject;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.repository.SubjectRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import com.studybuddy.studybuddy.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public List<TopicResponseDTO> getAllTopics(){
        return topicService.getAllTopics();
    }

    @PostMapping
    public TopicResponseDTO createTopic(@RequestBody TopicRequestDTO requestDTO)
    {
        return topicService.createTopic(requestDTO);
    }

    @PutMapping("/{id}")
    public TopicResponseDTO updateTopic(@PathVariable Long id, @RequestBody TopicRequestDTO requestDTO){
        return topicService.updateTopic(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id){
        topicService.deleteTopic(id);
    }

}
