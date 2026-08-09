package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.dto.FlashcardRequestDTO;
import com.studybuddy.studybuddy.dto.FlashcardResponseDTO;
import com.studybuddy.studybuddy.dto.GroqFlashcardRequest;
import com.studybuddy.studybuddy.entity.Flashcard;
import com.studybuddy.studybuddy.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @GetMapping
    public List<FlashcardResponseDTO> getAllFlashcards(){
        return flashcardService.getAllFlashcards();
    }

    @PostMapping
    public FlashcardResponseDTO createFlashcard(@RequestBody FlashcardRequestDTO requestDTO){
        return flashcardService.createFlashcard(requestDTO);
    }

    @PostMapping("/generate")
    public List<FlashcardResponseDTO> generateFlashcards(@RequestBody GroqFlashcardRequest requestDto) throws Exception{
        return flashcardService.generateFlashcardsForTopic(requestDto.getTopicId());
    }

    @PutMapping("/{id}")
    public FlashcardResponseDTO updateFlashcard(@PathVariable Long id, @RequestBody FlashcardRequestDTO requestDTO){
        return flashcardService.updateFlashcard(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteFlashcard(@PathVariable Long id){
        flashcardService.deleteFlashcard(id);
    }
}