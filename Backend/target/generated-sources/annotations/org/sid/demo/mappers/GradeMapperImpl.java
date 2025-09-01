package org.sid.demo.mappers;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.sid.demo.DTO.GradeDto;
import org.sid.demo.DTO.RegisterGradeRequest;
import org.sid.demo.DTO.UpdateGradeRequest;
import org.sid.demo.entities.Grade;
import org.sid.demo.entities.Submission;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T02:06:32+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class GradeMapperImpl implements GradeMapper {

    @Override
    public GradeDto toDto(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        String submissionId = null;
        Long id = null;
        Double score = null;
        String feedback = null;
        LocalDateTime givenAt = null;

        Long id1 = gradeSubmissionId( grade );
        if ( id1 != null ) {
            submissionId = String.valueOf( id1 );
        }
        id = grade.getId();
        score = grade.getScore();
        feedback = grade.getFeedback();
        givenAt = grade.getGivenAt();

        GradeDto gradeDto = new GradeDto( id, score, feedback, givenAt, submissionId );

        return gradeDto;
    }

    @Override
    public Grade toEntity(RegisterGradeRequest request) {
        if ( request == null ) {
            return null;
        }

        Grade grade = new Grade();

        grade.setFeedback( request.getFeedback() );
        grade.setScore( request.getScore() );

        grade.setGivenAt( java.time.LocalDateTime.now() );

        return grade;
    }

    @Override
    public void update(UpdateGradeRequest request, Grade grade) {
        if ( request == null ) {
            return;
        }

        if ( request.getFeedback() != null ) {
            grade.setFeedback( request.getFeedback() );
        }
        if ( request.getScore() != null ) {
            grade.setScore( request.getScore() );
        }
    }

    private Long gradeSubmissionId(Grade grade) {
        Submission submission = grade.getSubmission();
        if ( submission == null ) {
            return null;
        }
        return submission.getId();
    }
}
