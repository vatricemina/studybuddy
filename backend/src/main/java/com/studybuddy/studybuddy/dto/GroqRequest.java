package com.studybuddy.studybuddy.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroqRequest {
    private String model;
    private List<Message> messages;

    public GroqRequest(String prompt){
        this.model= "openai/gpt-oss-120b";
        this.messages=List.of(new Message("user", prompt));
    }

    public GroqRequest(String model, List<Message> messages){
        this.model=model;
        this.messages=messages;
    }


    @Data
    public static class Message{
        private String role;
        private String content;

        public Message(String role, String content){
            this.role=role;
            this.content=content;
        }
    }
}
