package org.yapp.error.code.auth;

import org.springframework.http.HttpStatus;
import org.yapp.error.dto.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
  OAUTH_ERROR(HttpStatus.FORBIDDEN, "Oauth Error"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
