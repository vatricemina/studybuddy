package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.FlashcardAIItem;
import com.studybuddy.studybuddy.dto.FlashcardRequestDTO;
import com.studybuddy.studybuddy.dto.FlashcardResponseDTO;
import com.studybuddy.studybuddy.entity.Flashcard;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.FlashcardRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import com.studybuddy.studybuddy.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private GroqService groqService;

    public List<FlashcardResponseDTO> getAllFlashcards(){
        User currentUser = currentUserService.getCurrentUser();
        return flashcardRepository.findByTopicSubjectUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public FlashcardResponseDTO createFlashcard(FlashcardRequestDTO requestDTO){
        Flashcard flashcard = toEntity(requestDTO);
        return toResponseDTO(flashcardRepository.save(flashcard));
    }

    public FlashcardResponseDTO updateFlashcard(Long id, FlashcardRequestDTO requestDTO){
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id " + id));

        checkOwnership(flashcard);

        flashcard.setQuestion(requestDTO.getQuestion());
        flashcard.setAnswer(requestDTO.getAnswer());
        flashcard.setTopic(getOwnedTopic(requestDTO.getTopicId()));

        return toResponseDTO(flashcardRepository.save(flashcard));
    }

    public void deleteFlashcard(Long id){
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id " + id));

        checkOwnership(flashcard);

        flashcardRepository.deleteById(id);
    }

    public List<FlashcardResponseDTO> generateFlashcardsForTopic(Long topicId) throws Exception{
        Topic topic=getOwnedTopic(topicId);

        List<FlashcardAIItem> items=groqService.generateFlashcards(topic.getTitle());

        List<Flashcard> savedFlashcards=new ArrayList<>();
        for(FlashcardAIItem item:items){
            Flashcard flashcard=new Flashcard();
            flashcard.setQuestion(item.getQuestion());
            flashcard.setAnswer(item.getAnswer());
            flashcard.setTopic(topic);
            savedFlashcards.add(flashcardRepository.save(flashcard));
        }
        return savedFlashcards.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Topic getOwnedTopic(Long topicId){
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + topicId));

        User currentUser = currentUserService.getCurrentUser();
        if (!topic.getSubject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't own this topic");
        }
        return topic;
    }

    private void checkOwnership(Flashcard flashcard){
        User currentUser = currentUserService.getCurrentUser();
        if (!flashcard.getTopic().getSubject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to modify this flashcard");
        }
    }

    private Flashcard toEntity(FlashcardRequestDTO dto){
        Topic topic = getOwnedTopic(dto.getTopicId());

        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(dto.getQuestion());
        flashcard.setAnswer(dto.getAnswer());
        flashcard.setTopic(topic);

        return flashcard;
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
}