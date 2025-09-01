package org.sid.demo.mappers;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.sid.demo.DTO.SubmissionDto;
import org.sid.demo.entities.Project;
import org.sid.demo.entities.Submission;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T02:06:31+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class SubmissionMapperImpl implements SubmissionMapper {

    @Override
    public SubmissionDto toDto(Submission submission) {
        if ( submission == null ) {
            return null;
        }

        String projectId = null;
        Long id = null;
        LocalDateTime submittedAt = null;

        Long id1 = submissionProjectId( submission );
        if ( id1 != null ) {
            projectId = String.valueOf( id1 );
        }
        id = submission.getId();
        submittedAt = submission.getSubmittedAt();

        SubmissionDto submissionDto = new SubmissionDto( id, submittedAt, projectId );

        return submissionDto;
    }

    private Long submissionProjectId(Submission submission) {
        Project project = submission.getProject();
        if ( project == null ) {
            return null;
        }
        return project.getId();
    }
}
