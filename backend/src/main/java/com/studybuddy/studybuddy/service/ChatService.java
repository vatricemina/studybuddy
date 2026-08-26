package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.ChatMessageDTO;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.TopicRepository;
import com.studybuddy.studybuddy.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    @Autowired
    private GroqService groqService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private TopicRepository topicRepository;

    public String chat(Long topicId, List<ChatMessageDTO> messages) throws Exception {
        String topicTitle = null;
        if (topicId != null) {
            Topic topic = getOwnedTopic(topicId);
            topicTitle = topic.getTitle();
        }
        return groqService.chat(topicTitle, messages);
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
}
