package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.SubjectRequestDTO;
import com.studybuddy.studybuddy.dto.SubjectResponseDTO;
import com.studybuddy.studybuddy.entity.Subject;
import com.studybuddy.studybuddy.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<SubjectResponseDTO> getAllSubjects(){
        return subjectRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SubjectResponseDTO createSubject(SubjectRequestDTO requestDTO){
        Subject subject = toEntity(requestDTO);
        Subject saved = subjectRepository.save(subject);
        return toResponseDTO(saved);
    }

    public SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO requestDTO){
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + id));

        subject.setName(requestDTO.getName());
        subject.setExamDate(requestDTO.getExamDate());
        subject.setDifficulty(requestDTO.getDifficulty());

        Subject saved = subjectRepository.save(subject);
        return toResponseDTO(saved);
    }

    public void deleteSubject(Long id){
        subjectRepository.deleteById(id);
    }

    private SubjectResponseDTO toResponseDTO(Subject subject){
        SubjectResponseDTO dto = new SubjectResponseDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setExamDate(subject.getExamDate());
        dto.setDifficulty(subject.getDifficulty());
        return dto;
    }

    private Subject toEntity(SubjectRequestDTO dto){
        Subject subject = new Subject();
        subject.setName(dto.getName());
        subject.setExamDate(dto.getExamDate());
        subject.setDifficulty(dto.getDifficulty());
        return subject;
    }
}