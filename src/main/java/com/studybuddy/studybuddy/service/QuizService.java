package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.QuizQuestionAIItem;
import com.studybuddy.studybuddy.dto.QuizQuestionResponseDTO;
import com.studybuddy.studybuddy.dto.QuizRequestDTO;
import com.studybuddy.studybuddy.dto.QuizResponseDTO;
import com.studybuddy.studybuddy.entity.Quiz;
import com.studybuddy.studybuddy.entity.QuizQuestion;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.QuizQuestionRepository;
import com.studybuddy.studybuddy.repository.QuizRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import com.studybuddy.studybuddy.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private GroqService groqService;

    @Autowired
    private QuizQuestionService quizQuestionService;

    public List<QuizResponseDTO> getAllQuizzes(){
        User currentUser = currentUserService.getCurrentUser();
        return quizRepository.findByTopicSubjectUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public QuizResponseDTO createQuiz(QuizRequestDTO requestDTO){
        Quiz quiz = toEntity(requestDTO);
        return toResponseDTO(quizRepository.save(quiz));
    }

    public QuizResponseDTO updateQuiz(Long id, QuizRequestDTO requestDTO){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + id));

        checkOwnership(quiz);

        quiz.setGeneratedAt(requestDTO.getGeneratedAt());
        quiz.setScore(requestDTO.getScore());
        quiz.setTopic(getOwnedTopic(requestDTO.getTopicId()));

        return toResponseDTO(quizRepository.save(quiz));
    }

    public void deleteQuiz(Long id){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + id));

        checkOwnership(quiz);

        quizRepository.deleteById(id);
    }

    public QuizResponseDTO generateQuizForTopic(Long topicId) throws Exception{
        Topic topic=getOwnedTopic(topicId);

        List<QuizQuestionAIItem> aiQuestions=groqService.generateQuizQuestions(topic.getTitle());

        Quiz quiz=new Quiz();
        quiz.setGeneratedAt(LocalDateTime.now());
        quiz.setScore(null);
        quiz.setTopic(topic);
        Quiz savedQuiz=quizRepository.save(quiz); //prvo sacuvaj  u bazu da bi svakom pitanju mogao dodijeliti ovaj kviz

        List<QuizQuestionResponseDTO> questionsDTOs=new ArrayList<>();
        for(QuizQuestionAIItem item:aiQuestions){
            QuizQuestion question =new QuizQuestion();
            question.setQuestionText(item.getQuestionText());
            question.setOptionA(item.getOptionA());
            question.setOptionB(item.getOptionB());
            question.setOptionC(item.getOptionC());
            question.setOptionD(item.getOptionD());
            question.setCorrectAnswer(item.getCorrectAnswer());
            question.setQuiz(savedQuiz);

            QuizQuestion savedQuestion=quizQuestionRepository.save(question);
            questionsDTOs.add(toQuestionResponseDTO(savedQuestion));
        }

        QuizResponseDTO responseDTO=toResponseDTO(savedQuiz);
        responseDTO.setQuestions(questionsDTOs);
        return responseDTO;
    }


    private QuizQuestionResponseDTO toQuestionResponseDTO (QuizQuestion quizQuestion){
        QuizQuestionResponseDTO dto=new QuizQuestionResponseDTO();
        dto.setId(quizQuestion.getId());
        dto.setQuestionText(quizQuestion.getQuestionText());
        dto.setCorrectAnswer(quizQuestion.getCorrectAnswer());
        dto.setOptionA(quizQuestion.getOptionA());
        dto.setOptionB(quizQuestion.getOptionB());
        dto.setOptionC(quizQuestion.getOptionC());
        dto.setOptionD(quizQuestion.getOptionD());
        dto.setQuizId(quizQuestion.getQuiz().getId());
        return dto;
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

    private void checkOwnership(Quiz quiz){
        User currentUser = currentUserService.getCurrentUser();
        if (!quiz.getTopic().getSubject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to modify this quiz");
        }
    }

    private Quiz toEntity(QuizRequestDTO dto){
        Topic topic = getOwnedTopic(dto.getTopicId());

        Quiz quiz = new Quiz();
        quiz.setGeneratedAt(dto.getGeneratedAt());
        quiz.setScore(dto.getScore());
        quiz.setTopic(topic);

        return quiz;
    }

    private QuizResponseDTO toResponseDTO(Quiz quiz){
        QuizResponseDTO dto = new QuizResponseDTO();
        dto.setId(quiz.getId());
        dto.setGeneratedAt(quiz.getGeneratedAt());
        dto.setScore(quiz.getScore());
        dto.setTopicId(quiz.getTopic().getId());
        dto.setTopicTitle(quiz.getTopic().getTitle());
        return dto;
    }
}