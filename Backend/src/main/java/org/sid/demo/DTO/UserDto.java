package org.sid.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor @Data
public class UserDto {
    private String login;
    private String name;
    private String email;
    private String avatarUrl;
    private String htmlUrl;

}
