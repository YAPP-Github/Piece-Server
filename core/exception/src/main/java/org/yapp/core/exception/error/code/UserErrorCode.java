package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
  INACTIVE_USER(HttpStatus.FORBIDDEN, "User is inactive"),
  NOTFOUND_USER(HttpStatus.NOT_FOUND, "User not found"),
  INVALID_OAUTH_PROVIDER(HttpStatus.INTERNAL_SERVER_ERROR, "유저의 OauthProvider가 올바르지 않습니다"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
