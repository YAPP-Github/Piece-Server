package org.yapp.domain.notification.dto.response;

import java.time.format.DateTimeFormatter;
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
  private String dateTime;

  public static NotificationHistoryResponse fromNotificationHistory(NotificationHistory history) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");
    String dateTime = history.getDateTime().format(formatter);

    return new NotificationHistoryResponse(history.getNotificationType(),
        history.getId(),
        history.getIsRead(),
        history.getTitle(),
        history.getBody(),
        dateTime);
  }
}
