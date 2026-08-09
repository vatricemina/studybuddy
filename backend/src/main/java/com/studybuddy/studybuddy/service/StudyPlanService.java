package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.StudyPlanAIItem;
import com.studybuddy.studybuddy.dto.StudyPlanEntryResponseDTO;
import com.studybuddy.studybuddy.entity.StudyPlanEntry;
import com.studybuddy.studybuddy.entity.Subject;
import com.studybuddy.studybuddy.entity.Topic;
import com.studybuddy.studybuddy.entity.User;
import com.studybuddy.studybuddy.repository.StudyPlanEntryRepository;
import com.studybuddy.studybuddy.repository.SubjectRepository;
import com.studybuddy.studybuddy.repository.TopicRepository;
import com.studybuddy.studybuddy.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyPlanService {
    @Autowired
    private StudyPlanEntryRepository studyPlanEntryRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private GroqService groqService;
    @Autowired
    private CurrentUserService currentUserService;

    public List<StudyPlanEntryResponseDTO> generatePlan(Long subjectId) throws Exception{
        Subject subject=getOwnedSubject(subjectId);

        List<Topic> topics=topicRepository.findBySubjectId(subjectId);
        if(topics.isEmpty()) throw new RuntimeException("This subject has no topics yet");

        List<StudyPlanAIItem> aiPlan=groqService.generateStudyPlan(subject.getName(), subject.getExamDate(), topics);

        List<StudyPlanEntry> savedEntries=new ArrayList<>();

        for(StudyPlanAIItem item:aiPlan){
            Topic matchingTopic=null;
            for(Topic t: topics){
                if(t.getTitle().equalsIgnoreCase(item.getTopicTitle())){
                    matchingTopic=t;
                    break;
                }
            }
            if(matchingTopic==null) continue;
            StudyPlanEntry entry=new StudyPlanEntry();
            entry.setPlannedDate(LocalDate.parse(item.getDate()));
            entry.setPlannedHours(item.getHours());
            entry.setTopic(matchingTopic);
            entry.setFocus(item.getFocus());

            savedEntries.add(studyPlanEntryRepository.save(entry));
        }
         return savedEntries.stream()
                 .map(this::toResponseDTO)
                 .collect(Collectors.toList());
    }

    public List<StudyPlanEntryResponseDTO> getPlanForSubject(Long subjectId){
        getOwnedSubject(subjectId);
        return studyPlanEntryRepository.findByTopicSubjectId(subjectId) //vrati listu studyplanentry-a
                .stream()//pusti tu listu na "traku" tj u tok koji mozemo obradjivati
                .map(this::toResponseDTO) //mapira svaki element u toku u responsedto, tj za svaki element primijeni ovu funkciju i zamijeni ga rezultatom
                .collect(Collectors.toList()); //spakuj sve nove transformirane objekte nazad u strukturu liste
    }

    private Subject getOwnedSubject(Long subjectId){
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + subjectId));

        User currentUser = currentUserService.getCurrentUser();
        if (!subject.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't own this subject");
        }
        return subject;
    }

    private StudyPlanEntryResponseDTO toResponseDTO(StudyPlanEntry entry){
        StudyPlanEntryResponseDTO dto=new StudyPlanEntryResponseDTO();
        dto.setId(entry.getId());
        dto.setPlannedDate(entry.getPlannedDate());
        dto.setPlannedHours(entry.getPlannedHours());
        dto.setTopicId(entry.getTopic().getId());
        dto.setTopicTitle(entry.getTopic().getTitle());
        dto.setFocus(entry.getFocus());
        return dto;
    }
}
