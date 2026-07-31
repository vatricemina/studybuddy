package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping
    public List<Topic> getAllTopics(){
        return topicRepository.findAll();
    }

    @PostMapping
    public Topic createTopic(@RequestBody Topic topic){
        return topicRepository.save(topic);
    }

    @PutMapping("/{id}")
    public Topic updateTopic(@PathVariable Long id, @RequestBody Topic updatedTopic){
        Topic topic=topicRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Topic not found with id "+id));

        topic.setTitle(updatedTopic.getTitle());
        topic.setEstimatedHours(updatedTopic.getEstimatedHours());
        topic.setCompleted(updatedTopic.getCompleted());

        return topicRepository.save(topic);
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id){
        topicRepository.deleteById(id);
    }
}
