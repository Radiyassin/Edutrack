package org.sid.demo.mappers;

import javax.annotation.processing.Generated;
import org.sid.demo.DTO.RegisterUserRequest;
import org.sid.demo.DTO.UpdateUserRequest;
import org.sid.demo.DTO.UserDto;
import org.sid.demo.entities.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T02:06:33+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        String email = null;
        String name = null;

        email = user.getEmail();
        name = user.getName();

        String login = null;
        String avatarUrl = null;
        String htmlUrl = null;

        UserDto userDto = new UserDto( login, name, email, avatarUrl, htmlUrl );

        return userDto;
    }

    @Override
    public User toEntity(RegisterUserRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( request.getEmail() );
        user.setName( request.getName() );

        return user;
    }

    @Override
    public void update(UpdateUserRequest request, User user) {
        if ( request == null ) {
            return;
        }

        if ( request.getEmail() != null ) {
            user.setEmail( request.getEmail() );
        }
        if ( request.getName() != null ) {
            user.setName( request.getName() );
        }
    }
}
