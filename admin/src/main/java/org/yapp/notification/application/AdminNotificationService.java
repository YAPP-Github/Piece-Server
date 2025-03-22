package org.yapp.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.notification.enums.NotificationType;
import org.yapp.core.notification.application.NotificationService;
import org.yapp.core.notification.dao.FcmTokenRepository;

@Service
@RequiredArgsConstructor
public class AdminNotificationService {

  private final NotificationService notificationService;
  private final FcmTokenRepository fcmTokenRepository;

  @Transactional
  public void sendProfileApprovedNotification(Long userId) {
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
      notificationService.sendNotification("fcm", fcmToken.getToken(), userId,
          NotificationType.PROFILE_APPROVED, "가입이 승인되었어요!", "매일 밤 10시, 설레는 인연을 만나보세요!");
    });
  }

  @Transactional
  public void sendProfileRejectedNotification(Long userId) {
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
      notificationService.sendNotification("fcm", fcmToken.getToken(), userId,
          NotificationType.PROFILE_REJECTED, "프로필이 반려되었어요", "반려 사유를 확인하고 수정해보세요!");
    });
  }
}
