package org.yapp.core.notification.application;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.notification.NotificationHistory;
import org.yapp.core.domain.notification.enums.NotificationType;
import org.yapp.core.notification.client.NotificationClient;
import org.yapp.core.notification.dao.NotificationHistoryRepository;
import org.yapp.core.notification.provider.NotificationProvider;

@Service
@RequiredArgsConstructor
public class CommonNotificationService {

  private final NotificationProvider notificationProvider;
  private final NotificationHistoryRepository notificationHistoryRepository;

  @Transactional
  public void sendNotification(String clientName, String token, Long userId,
      NotificationType notificationType, String title, String body) {
    Map<String, String> data = new HashMap<>();
    NotificationClient notificationClient = notificationProvider.getNotificationClient(
        clientName);

    NotificationHistory notificationHistory = NotificationHistory.builder()
        .userId(userId)
        .notificationType(notificationType)
        .title(title)
        .body(body)
        .isRead(false)
        .dateTime(LocalDateTime.now())
        .build();
    
    NotificationHistory newNotificationHistory = notificationHistoryRepository.save(
        notificationHistory);
    data.put("notificationId", String.valueOf(newNotificationHistory.getId()));

    notificationClient.sendNotificationWithData(token, title, body, data);
  }


}

