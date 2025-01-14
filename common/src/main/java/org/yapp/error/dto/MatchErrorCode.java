package org.yapp.error.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchErrorCode implements ErrorCode {
  NOTFOUND_MATCH(HttpStatus.NOT_FOUND, "Match not found"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
