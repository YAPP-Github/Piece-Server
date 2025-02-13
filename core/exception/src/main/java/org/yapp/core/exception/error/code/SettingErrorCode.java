package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SettingErrorCode implements ErrorCode {
  NOT_FOUND_SETTING(HttpStatus.BAD_REQUEST, "설정 정보가 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
