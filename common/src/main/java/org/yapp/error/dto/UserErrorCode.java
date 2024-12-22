package org.yapp.error.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
  INACTIVE_USER(HttpStatus.FORBIDDEN, "User is inactive"),
  NOTFOUND_USER(HttpStatus.NOT_FOUND, "User not found"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
