package org.sid.demo.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data

public class RegisterGradeRequest {
    @NotNull(message = "Score is required")
    @DecimalMin(value = "1.0", message = "Score can't be lower than 1.0")
    @DecimalMax(value = "100.0", message = "Score cannot exceed 5.0")
    private Double score;

    @Size(max = 500, message = "Feedback cannot exceed 500 characters")
    private String feedback;

}
