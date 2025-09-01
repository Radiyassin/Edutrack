package org.sid.demo.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.sid.demo.DTO.RegisterUserRequest;
import org.sid.demo.DTO.UpdateUserRequest;
import org.sid.demo.DTO.UserDto;
import org.sid.demo.entities.User;

@Mapper(componentModel = "spring" , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);

}