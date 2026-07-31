package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.TopicRequestDTO;
import com.studybuddy.studybuddy.dto.TopicResponseDTO;
import com.studybuddy.studybuddy.entity.StudySession;
import com.studybuddy.studybuddy.entity.Subject;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.repository.SubjectRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public List<TopicResponseDTO> getAllTopics(){
        return topicRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public TopicResponseDTO createTopic(@RequestBody TopicRequestDTO requestDTO)
    {
        Topic topic=toEntity(requestDTO);
        return toResponseDTO(topicRepository.save(topic));
    }

    @PutMapping("/{id}")
    public TopicResponseDTO updateTopic(@PathVariable Long id, @RequestBody TopicRequestDTO requestDTO){
        Topic topic=topicRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Topic not found with id "+id));

        topic.setTitle(requestDTO.getTitle());
        topic.setEstimatedHours(requestDTO.getEstimatedHours());
        topic.setCompleted(requestDTO.getCompleted());

        Subject subject=subjectRepository.findById(requestDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + requestDTO.getSubjectId()));
        topic.setSubject(subject);

        Topic saved=topicRepository.save(topic);

        return toResponseDTO(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id){
        topicRepository.deleteById(id);
    }


    //pomocne metode
    private TopicResponseDTO toResponseDTO(Topic topic){
        TopicResponseDTO dto=new TopicResponseDTO();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setEstimatedHours(topic.getEstimatedHours());
        dto.setCompleted(topic.getCompleted());
        dto.setSubjectId(topic.getSubject().getId());
        dto.setSubjectName(topic.getSubject().getName());
        return dto;
    }

    private Topic toEntity(TopicRequestDTO dto){
        Topic topic=new Topic();
        topic.setTitle(dto.getTitle());
        topic.setEstimatedHours(dto.getEstimatedHours());
        topic.setCompleted(dto.getCompleted());

        Subject subject=subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + dto.getSubjectId()));
        topic.setSubject(subject);
        return topic;
    }

}
