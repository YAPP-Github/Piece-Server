package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BlockContactErrorCode implements ErrorCode {
  BLOCK_SYNC_TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "연락처 차단 동기화 시간 정보가 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
