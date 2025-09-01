package org.sid.demo.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.sid.demo.DTO.GradeDto;
import org.sid.demo.DTO.RegisterGradeRequest;
import org.sid.demo.DTO.UpdateGradeRequest;
import org.sid.demo.entities.Grade;

@Mapper(componentModel = "spring" , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GradeMapper {
    @Mapping(source = "submission.id", target = "submissionId")
    GradeDto toDto(Grade grade);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submission", ignore = true)
    @Mapping(target = "givenAt", expression = "java(java.time.LocalDateTime.now())")
    Grade toEntity(RegisterGradeRequest request);

    void update(UpdateGradeRequest request , @MappingTarget Grade grade);
}
