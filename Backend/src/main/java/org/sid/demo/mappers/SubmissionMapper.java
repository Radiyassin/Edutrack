package org.sid.demo.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.sid.demo.DTO.SubmissionDto;
import org.sid.demo.entities.Submission;

@Mapper(componentModel = "spring" , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SubmissionMapper {
    @Mapping(source = "project.id", target = "projectId")
    SubmissionDto toDto(Submission submission);

}
