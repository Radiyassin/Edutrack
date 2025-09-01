package org.sid.demo.services;


import lombok.AllArgsConstructor;
import org.sid.demo.DTO.GradeDto;
import org.sid.demo.DTO.RegisterGradeRequest;
import org.sid.demo.DTO.UpdateGradeRequest;
import org.sid.demo.Exception.DuplicateGradeException;
import org.sid.demo.Exception.GradeNotFoundException;
import org.sid.demo.Exception.SubmissionNotFoundException;
import org.sid.demo.mappers.GradeMapper;
import org.sid.demo.repositories.GradeRepository;
import org.sid.demo.repositories.SubmissionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;
    private final SubmissionRepository submissionRepository;

    public Iterable<GradeDto> getAllGrades(String sortBy) {
        if (!Set.of("score" , "id").contains(sortBy)) {
            sortBy = "score";
        }

        var grades = gradeRepository.findAll(Sort.by(sortBy));
        return grades.stream()
                .map(gradeMapper::toDto)
                .toList();
    }

    public GradeDto getGrade(Long id){
        var grade = gradeRepository.findById(id).orElseThrow(GradeNotFoundException::new);
        return gradeMapper.toDto(grade);
    }

    public GradeDto getSubmissionGrade(Long id){
        var submission = submissionRepository.findById(id).orElseThrow(SubmissionNotFoundException::new);
        var grade = gradeRepository.findBySubmissionId(id).orElseThrow(GradeNotFoundException::new);

        return gradeMapper.toDto(grade);
    }

    public GradeDto registerGrade (Long submissionId, RegisterGradeRequest request){

        var submission = submissionRepository.findById(submissionId).orElseThrow(SubmissionNotFoundException::new);

        if(gradeRepository.existsBySubmissionId(submissionId)){
            throw new DuplicateGradeException();
        }

        var grade = gradeMapper.toEntity(request);
        grade.setSubmission(submission);
        gradeRepository.save(grade);
        return gradeMapper.toDto(grade);

    }

    public GradeDto updateGrade(Long submissionId, UpdateGradeRequest request){
        var submission = submissionRepository.findById(submissionId).orElseThrow(SubmissionNotFoundException::new);

        var grade = gradeRepository.findBySubmissionId(submissionId).orElseThrow(GradeNotFoundException::new);

        gradeMapper.update(request,grade);
        gradeRepository.save(grade);

        return gradeMapper.toDto(grade);
    }

    public void deleteGrade(Long id){
        var grade = gradeRepository.findById(id).orElseThrow(GradeNotFoundException::new);
        gradeRepository.delete(grade);
    }
}
