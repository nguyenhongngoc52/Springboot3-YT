package com.example.springboot3youtube.exception;

import com.example.springboot3youtube.dto.request.ApiRespone;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final String MIN_ATTRIBUTE = "min";

  @ExceptionHandler(value = Exception.class)
  ResponseEntity<ApiRespone> handlingException(Exception exception) {
    ApiRespone apiRespone = new ApiRespone();
    apiRespone.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    apiRespone.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
    return ResponseEntity.badRequest().body(apiRespone);
  }

  @ExceptionHandler(value = AppException.class)
  ResponseEntity<ApiRespone> handlingAppException(AppException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    ApiRespone apiRespone = new ApiRespone();
    apiRespone.setCode(errorCode.getCode());
    apiRespone.setMessage(errorCode.getMessage());
    return ResponseEntity
         .status(errorCode.getStatusCode())
         .body(apiRespone);
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  ResponseEntity<ApiRespone> handlingAccessDeniedException(AccessDeniedException exception) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    return ResponseEntity
         .status(errorCode.getStatusCode())
         .body(
              ApiRespone.builder()
                   .code(errorCode.getCode())
                   .message(errorCode.getMessage())
                   .build()
         );
  }


  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  ResponseEntity<ApiRespone> handlingValidationException(MethodArgumentNotValidException exception) {
    String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage(); // lấy key enum được khai báo   @Size(min = 3 , message = "USERNAME_INVALID")
    ErrorCode errorCode = ErrorCode.INVALID_KEY;
    Map<String, Object> attributes = null;
    try {
      errorCode = ErrorCode.valueOf(enumKey);
      var constrainVialation = exception.getBindingResult()
           .getAllErrors().get(0).unwrap(ConstraintViolation.class);
      attributes = constrainVialation.getConstraintDescriptor().getAttributes();


    } catch (IllegalArgumentException e) {

    }

    ApiRespone apiRespone = new ApiRespone();
    apiRespone.setCode(errorCode.getCode());
    apiRespone.setMessage(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());
    return ResponseEntity.badRequest().body(apiRespone);
  }

  private String mapAttribute(String message, Map<String, Object> attributes) {
    String minValue = attributes.get(MIN_ATTRIBUTE).toString();
    return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
  }
}
