package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.TopicRequestDTO;
import com.studybuddy.studybuddy.dto.TopicResponseDTO;
import com.studybuddy.studybuddy.entity.*;
import com.studybuddy.studybuddy.repository.*;
import com.studybuddy.studybuddy.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private StudyPlanEntryRepository studyPlanEntryRepository;

    public List<TopicResponseDTO> getAllTopics(){
        User currentUser=currentUserService.getCurrentUser();
        return topicRepository.findBySubjectUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    public TopicResponseDTO createTopic(TopicRequestDTO requestDTO)
    {
        Topic topic=toEntity(requestDTO);
        return toResponseDTO(topicRepository.save(topic));
    }

    public TopicResponseDTO updateTopic(Long id, TopicRequestDTO requestDTO){
        Topic topic=topicRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Topic not found with id "+id));

        checkOwnership(topic);

        topic.setTitle(requestDTO.getTitle());
        topic.setEstimatedHours(requestDTO.getEstimatedHours());
        topic.setCompleted(requestDTO.getCompleted());

        topic.setSubject(getOwnedSubject(requestDTO.getSubjectId()));

        return toResponseDTO(topicRepository.save(topic));
    }

    public void deleteTopic(Long id){
        Topic topic = getOwnedTopic(id);
        List<Quiz> quizzes = quizRepository.findByTopicId(id);
        for (Quiz quiz : quizzes) {
            List<QuizQuestion> questions = quizQuestionRepository.findByQuizId(quiz.getId());
            quizQuestionRepository.deleteAll(questions);
        }
        quizRepository.deleteAll(quizzes);
        flashcardRepository.deleteAll(flashcardRepository.findByTopicId(id));
        studySessionRepository.deleteAll(studySessionRepository.findByTopicId(id));
        studyPlanEntryRepository.deleteAll(studyPlanEntryRepository.findByTopicId(id));
        topicRepository.deleteById(id);

    }


    //pomocne metode
    private Topic getOwnedTopic(Long topicId){
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + topicId));

        User currentUser = currentUserService.getCurrentUser();
        if (!topic.getSubject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't own this topic");
        }
        return topic;
    }

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

    private Subject getOwnedSubject(Long subjectId){ //vraca predmet iz zahtjeva ako ga trenutni korisnik zaista ima u bazi svojih predmeta
        Subject subject=subjectRepository.findById(subjectId) //da li uopce taj predmet postoji s tim idem
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + subjectId));
        User currentUser=currentUserService.getCurrentUser();
        if(!subject.getUser().getId().equals(currentUser.getId())){ //da li ga trenutni korisnik posjeduje
            throw new RuntimeException("You don't own this subject");
        }
        return subject;

    }

    private void checkOwnership(Topic topic){
        User currentUser=currentUserService.getCurrentUser();
        if(!topic.getSubject().getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to modify this topic");
        }
    }

    private Topic toEntity(TopicRequestDTO dto){
        Subject subject=getOwnedSubject(dto.getSubjectId());
        Topic topic=new Topic();
        topic.setTitle(dto.getTitle());
        topic.setEstimatedHours(dto.getEstimatedHours());
        topic.setCompleted(dto.getCompleted());

        topic.setSubject(subject);
        return topic;
    }

}
