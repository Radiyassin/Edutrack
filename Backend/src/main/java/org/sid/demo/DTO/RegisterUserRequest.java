package org.sid.demo.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.sid.demo.validations.Lowercase;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Lowercase(message = "Email must be in lowercase")
    private String email;

}
