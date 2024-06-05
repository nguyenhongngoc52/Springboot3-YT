package com.example.springboot3youtube.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRespone {
  private Integer id;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private LocalDate dob;
}
