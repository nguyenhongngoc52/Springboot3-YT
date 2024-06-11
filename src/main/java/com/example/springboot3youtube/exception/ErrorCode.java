package com.example.springboot3youtube.exception;

public enum ErrorCode {
  USER_EXISTED(1001 , "User existed"),
  USER_NOT_EXISTED(1005 , "User not existed"),
  UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error"),
  USERNAME_INVALID(1003,"UserName must be at least 3 characters"),

  INVALID_PASSWORD(1004,"Password must be at least 8 characters"),

  INVALID_KEY(0001,"Invalid message key"),
  UNAUTHENTICATED(1006,"Unauthenticated")

  ;
  private int code ;
  private String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
