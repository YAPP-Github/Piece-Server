package org.yapp.domain.notification.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yapp.domain.notification.application.ApiNotificationService;
import org.yapp.domain.notification.application.NotificationQueueService;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

  private final ApiNotificationService apiNotificationService;
  private final NotificationQueueService notificationQueueService;

  @Scheduled(cron = "0 0 22 * * *")
  public void sendMatchNotifications() {
    while (true) {
      Long userId = notificationQueueService.popUserIdFromMatchNotificationQueue();
      if (userId == null) {
        break;
      }
      apiNotificationService.sendNewMatchNotification(userId);
    }
  }
}
