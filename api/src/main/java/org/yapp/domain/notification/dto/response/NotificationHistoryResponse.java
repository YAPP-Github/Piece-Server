package org.yapp.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.yapp.core.domain.notification.NotificationHistory;
import org.yapp.core.domain.notification.enums.NotificationType;

@AllArgsConstructor
@Getter
public class NotificationHistoryResponse {

  private NotificationType notificationType;
  private Long notificationId;
  private Boolean isRead;
  private String title;
  private String body;

  public static NotificationHistoryResponse fromNotificationHistory(NotificationHistory history) {
    return new NotificationHistoryResponse(history.getNotificationType(),
        history.getId(),
        history.getIsRead(),
        history.getTitle(),
        history.getBody());
  }
}
