package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.FlashcardRequestDTO;
import com.studybuddy.studybuddy.dto.FlashcardResponseDTO;
import com.studybuddy.studybuddy.entity.Flashcard;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.repository.FlashcardRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<FlashcardResponseDTO> getAllFlashcards(){
        return flashcardRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public FlashcardResponseDTO createFlashcard(FlashcardRequestDTO requestDTO){
        return toResponseDTO(flashcardRepository.save(toEntity(requestDTO)));
    }

    public FlashcardResponseDTO updateFlashcard(Long id, FlashcardRequestDTO requestDTO){
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id " + id));

        flashcard.setQuestion(requestDTO.getQuestion());
        flashcard.setAnswer(requestDTO.getAnswer());

        Topic topic = topicRepository.findById(requestDTO.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + requestDTO.getTopicId()));
        flashcard.setTopic(topic);

        return toResponseDTO(flashcardRepository.save(flashcard));
    }

    public void deleteFlashcard(Long id){
        flashcardRepository.deleteById(id);
    }

    private FlashcardResponseDTO toResponseDTO(Flashcard flashcard){
        FlashcardResponseDTO dto = new FlashcardResponseDTO();
        dto.setId(flashcard.getId());
        dto.setQuestion(flashcard.getQuestion());
        dto.setAnswer(flashcard.getAnswer());
        dto.setTopicId(flashcard.getTopic().getId());
        dto.setTopicTitle(flashcard.getTopic().getTitle());
        return dto;
    }

    private Flashcard toEntity(FlashcardRequestDTO dto){
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(dto.getQuestion());
        flashcard.setAnswer(dto.getAnswer());

        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + dto.getTopicId()));
        flashcard.setTopic(topic);

        return flashcard;
    }
}