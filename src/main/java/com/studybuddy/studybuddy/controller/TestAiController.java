package com.studybuddy.studybuddy.controller;

import com.studybuddy.studybuddy.service.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-ai")
public class TestAiController {

    @Autowired
    private GroqService groqService;

    @GetMapping
    public String test() throws Exception {
        return groqService.generateContent("Reci mi zdravo na bosanskom jeziku, kratko.");
    }
}