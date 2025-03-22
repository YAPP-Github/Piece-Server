package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements ErrorCode {
  FCM_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 전송에 실패했습니다."),
  FCM_INIT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 초기화에 실패했습니다"),
  FCM_TOKEN_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 토큰이 존재하지 않습니다"),

  NOTIFICATION_CLIENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "알림 클라이언트를 찾을 수 없습니다."),
  NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 알림 ID입니다."),
  NOT_OWNED_NOTIFICATION(HttpStatus.BAD_REQUEST, "자신의 알림이 아닙니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
