package org.yapp.domain.notification.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.notification.NotificationHistory;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.NotificationErrorCode;
import org.yapp.core.notification.dao.NotificationHistoryRepository;
import org.yapp.domain.notification.dto.response.NotificationHistoryResponse;

@RequiredArgsConstructor
@Service
public class NotificationHistoryService {

  private static final int PAGE_COUNT = 10;
  private final NotificationHistoryRepository notificationHistoryRepository;

  public List<NotificationHistoryResponse> getNotificationHistories(Long userId, Long cursor) {
    Pageable pageable = PageRequest.of(0, PAGE_COUNT);
    List<NotificationHistory> notifications = notificationHistoryRepository.getNotificationHistories(
        cursor,
        userId, pageable);
    return notifications.stream().map(NotificationHistoryResponse::fromNotificationHistory)
        .toList();
  }

  @Transactional
  public void checkNotificationRead(Long userId, Long notificationId) {
    NotificationHistory notificationHistory = notificationHistoryRepository.findById(notificationId)
        .orElseThrow(() -> new ApplicationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));
    if (!notificationHistory.getUserId().equals(userId)) {
      throw new ApplicationException(NotificationErrorCode.NOT_OWNED_NOTIFICATION);
    }
    notificationHistory.checkRead();
  }
}
