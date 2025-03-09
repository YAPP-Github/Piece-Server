package enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationClientName {
  FCM_CLIENT("fcm"),
  APM_CLIENT("apn"),
  ;

  private final String clientName;
}
