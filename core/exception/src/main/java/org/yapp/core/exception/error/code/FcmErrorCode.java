package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FcmErrorCode implements ErrorCode {
  FCM_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 전송에 실패했습니다."),
  FCM_INIT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 초기화에 실패했습니다"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
