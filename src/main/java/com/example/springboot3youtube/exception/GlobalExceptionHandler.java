package com.example.springboot3youtube.exception;

import com.example.springboot3youtube.dto.request.ApiRespone;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
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
    ErrorCode errorCode = ErrorCode.valueOf(enumKey);
    ApiRespone apiRespone = new ApiRespone();
    apiRespone.setCode(errorCode.getCode());
    apiRespone.setMessage(errorCode.getMessage());
    return ResponseEntity.badRequest().body(apiRespone);
  }
}
