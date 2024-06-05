package com.example.springboot3youtube.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // những thông tin null thì ko cần show ra ở json trả về
public class ApiRespone <T> {
  private int code;
  private String message;
  private T result ;
}
