package org.yapp.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.yapp.core.domain.notification.NotificationHistory;
import org.yapp.core.domain.notification.enums.NotificationType;

@AllArgsConstructor
@Getter
public class NotificationResponse {

  private NotificationType notificationType;
  private Long notificationId;
  private Boolean isRead;
  private String title;
  private String body;

  public static NotificationResponse fromNotificationHistory(NotificationHistory history) {
    return new NotificationResponse(history.getNotificationType(),
        history.getId(),
        history.getIsRead(),
        history.getTitle(),
        history.getBody());
  }
}
