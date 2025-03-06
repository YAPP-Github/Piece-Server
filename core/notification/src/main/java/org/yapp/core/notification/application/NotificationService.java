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
import org.yapp.core.notification.dao.FcmTokenRepository;
import org.yapp.core.notification.dao.NotificationHistoryRepository;
import org.yapp.core.notification.provider.NotificationProvider;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationProvider notificationProvider;
  private final FcmTokenRepository fcmTokenRepository;
  private final NotificationHistoryRepository notificationHistoryRepository;

  @Transactional
  public void sendNotification(String clientName, String token, Long userId,
      NotificationType notificationType, String title, String body) {
    Map<String, String> data = new HashMap<>();
    NotificationClient notificationClient = notificationProvider.getNotificationClient(
        clientName);

    NotificationHistory notificationHistory = new NotificationHistory(userId, notificationType,
        title, body, false, LocalDateTime.now());
    NotificationHistory newNotificationHistory = notificationHistoryRepository.save(
        notificationHistory);
    data.put("notificationId", String.valueOf(newNotificationHistory.getId()));

    notificationClient.sendNotificationWithData(token, title, body, data);
  }

  @Transactional
  public void sendNewMatchNotification(Long userId) {
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
      sendNotification("fcm", fcmToken.getToken(), userId,
          NotificationType.MATCH_NEW, "새로운 인연 도착!", "지금 상대방과의 퍼즐 조각을 맞춰보세요.");
    });

  }

  @Transactional
  public void sendMatchAcceptedNotification(Long userId) {
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
      sendNotification("fcm", fcmToken.getToken(), userId,
          NotificationType.MATCH_ACCEPTED, "누군가 먼저 다가왔어요!", "당신을 기다리는 조각이 있어요. 마음이 닿으면 응답해보세요.");
    });
  }

  @Transactional
  public void sendMatchCompletedNotification(Long userId, String matchedUserName) {
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
          String body = matchedUserName + "님과 연락처가 공개되었어요. 대화를 시작해보세요!";
          sendNotification("fcm", fcmToken.getToken(), userId,
              NotificationType.MATCH_COMPLETED, "퍼즐이 완성되었어요!", body);
        }
    );
  }

  @Transactional
  public void sendProfileApprovedNotification(Long userId) {
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
      sendNotification("fcm", fcmToken.getToken(), userId,
          NotificationType.PROFILE_APPROVED, "가입이 승인되었어요!", "매일 밤 10시, 설레는 인연을 만나보세요!");
    });
  }

  @Transactional
  public void sendProfileRejectedNotification(Long userId) {
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
      sendNotification("fcm", fcmToken.getToken(), userId,
          NotificationType.PROFILE_REJECTED, "프로필이 반려되었어요", "반려 사유를 확인하고 수정해보세요!");
    });
  }
}

