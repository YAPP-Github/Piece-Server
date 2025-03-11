package org.yapp.domain.notification.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yapp.core.domain.notification.NotificationHistory;
import org.yapp.core.notification.dao.NotificationHistoryRepository;
import org.yapp.domain.notification.dto.response.NotificationResponse;

@RequiredArgsConstructor
@Service
public class NotificationCenterService {

  private static final int PAGE_COUNT = 10;
  private final NotificationHistoryRepository notificationHistoryRepository;

  public List<NotificationResponse> getNotifications(Long userId, Long cursor) {
    Pageable pageable = PageRequest.of(0, PAGE_COUNT);
    List<NotificationHistory> notifications = notificationHistoryRepository.getNotifications(cursor,
        userId, pageable);
    return notifications.stream().map(NotificationResponse::fromNotificationHistory).toList();
  }
}
