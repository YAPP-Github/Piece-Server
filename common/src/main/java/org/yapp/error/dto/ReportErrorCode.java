package org.yapp.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements ErrorCode {
  ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "이미 신고한 유저입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
