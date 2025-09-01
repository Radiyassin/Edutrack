package org.sid.demo.mappers;

import org.mapstruct.*;
import org.sid.demo.DTO.ProjectDto;
import org.sid.demo.DTO.RegisterProjectRequest;
import org.sid.demo.DTO.UpdateProjectRequest;
import org.sid.demo.entities.Project;
import org.sid.demo.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring" , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {

    @Mapping(target = "memberIds", source = "members", qualifiedByName = "mapMembersToIds")
    ProjectDto toDto(Project project);

    @Named("mapMembersToIds")
    default List<String> mapMembersToIds(List<User> members) {
        if (members == null) {
            return List.of();
        }
        return members.stream()
                .map(user -> String.valueOf(user.getId()))
                .collect(Collectors.toList());
    }


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Project toEntity(RegisterProjectRequest request);

    void update(UpdateProjectRequest request, @MappingTarget Project project);

}
