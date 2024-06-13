package com.example.springboot3youtube.dto.request;

import com.example.springboot3youtube.exception.ErrorCode;
import com.example.springboot3youtube.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3 , message = "USERNAME_INVALID")
    private String username;
    @Size(min = 8 , message = "INVALID_PASSWORD")
    private String password;
    private String firstName;
    private String lastName;
    @DobConstraint(min = 18,message = "INVALID_DOB")
    private LocalDate dob;

}
