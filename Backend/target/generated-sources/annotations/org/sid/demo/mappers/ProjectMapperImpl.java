package org.sid.demo.mappers;

import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.processing.Generated;
import org.sid.demo.DTO.ProjectDto;
import org.sid.demo.DTO.RegisterProjectRequest;
import org.sid.demo.DTO.UpdateProjectRequest;
import org.sid.demo.entities.Project;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T02:06:33+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectDto toDto(Project project) {
        if ( project == null ) {
            return null;
        }

        List<String> memberIds = null;
        Long id = null;
        String title = null;
        String description = null;
        LocalDateTime createdAt = null;

        memberIds = mapMembersToIds( project.getMembers() );
        id = project.getId();
        title = project.getTitle();
        description = project.getDescription();
        createdAt = project.getCreatedAt();

        ProjectDto projectDto = new ProjectDto( id, title, description, createdAt, memberIds );

        return projectDto;
    }

    @Override
    public Project toEntity(RegisterProjectRequest request) {
        if ( request == null ) {
            return null;
        }

        Project project = new Project();

        project.setDescription( request.getDescription() );
        project.setTitle( request.getTitle() );

        project.setCreatedAt( java.time.LocalDateTime.now() );

        return project;
    }

    @Override
    public void update(UpdateProjectRequest request, Project project) {
        if ( request == null ) {
            return;
        }

        if ( request.getDescription() != null ) {
            project.setDescription( request.getDescription() );
        }
        if ( request.getTitle() != null ) {
            project.setTitle( request.getTitle() );
        }
    }
}
