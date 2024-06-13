package com.example.springboot3youtube.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
  USER_EXISTED(1002 , "User existed", HttpStatus.INTERNAL_SERVER_ERROR),
  USER_NOT_EXISTED(1005 , "User not existed" ,HttpStatus.NOT_FOUND),
  UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error",HttpStatus.BAD_REQUEST),
  USERNAME_INVALID(1003,"UserName must be at least 3 characters",HttpStatus.BAD_REQUEST),

  INVALID_PASSWORD(1004,"Password must be at least 8 characters",HttpStatus.BAD_REQUEST),

  INVALID_KEY(1001,"Invalid message key",HttpStatus.BAD_REQUEST),
  UNAUTHENTICATED(1006,"Unauthenticated",HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1007,"You do not have permission",HttpStatus.FORBIDDEN),
  INVALID_DOB(1008,"Invalid date of birth {min}",HttpStatus.BAD_REQUEST)
  ;
  private int code ;
  private String message;

  private HttpStatusCode statusCode;

  ErrorCode(int code, String message,HttpStatusCode statusCode) {
    this.code = code;
    this.message = message;
    this.statusCode = statusCode;
  }

}
