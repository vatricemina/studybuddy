package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.entity.Flashcard;
import com.studybuddy.studybuddy.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @GetMapping
    public List<Flashcard> getAllFlashcards(){return flashcardRepository.findAll();}

    @PostMapping
    public Flashcard createFlashcard(@RequestBody Flashcard flashcard){return flashcardRepository.save(flashcard);}

    @PutMapping("/{id}")
    public Flashcard updateFlashcard(@PathVariable Long id, @RequestBody Flashcard updatedFlashcard){
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id " + id));

        flashcard.setQuestion(updatedFlashcard.getQuestion());
        flashcard.setAnswer(updatedFlashcard.getAnswer());
        flashcard.setTopic(updatedFlashcard.getTopic());

        return flashcardRepository.save(flashcard);
    }

    @DeleteMapping("/{id}")
    public void deleteFlashcard(@PathVariable Long id){
        flashcardRepository.deleteById(id);
    }
}