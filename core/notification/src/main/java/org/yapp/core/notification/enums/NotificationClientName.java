package org.yapp.core.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationClientName {
  FCM_CLIENT("fcm"),
  ;

  private final String clientName;
}
