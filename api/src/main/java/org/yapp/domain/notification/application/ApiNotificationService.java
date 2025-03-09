package org.yapp.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.notification.enums.NotificationType;
import org.yapp.core.domain.setting.Setting;
import org.yapp.core.notification.application.CommonNotificationService;
import org.yapp.core.notification.dao.FcmTokenRepository;
import org.yapp.domain.setting.application.SettingService;

@RequiredArgsConstructor
@Service
public class ApiNotificationService {

  private final CommonNotificationService commonNotificationService;
  private final FcmTokenRepository fcmTokenRepository;
  private final SettingService settingService;

  @Transactional
  public void sendNewMatchNotification(Long userId) {
    if (!isUserAllowingMatchNotification(userId)) {
      return;
    }
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
      commonNotificationService.sendNotification("fcm", fcmToken.getToken(), userId,
          NotificationType.MATCH_NEW, "새로운 인연 도착!", "지금 상대방과의 퍼즐 조각을 맞춰보세요.");
    });

  }

  private boolean isUserAllowingMatchNotification(Long userId) {
    Setting userSetting = settingService.getUserSetting(userId);
    return userSetting.isNotification() && userSetting.isMatchNotification();
  }

  @Transactional
  public void sendMatchAcceptedNotification(Long userId) {
    if (!isUserAllowingMatchNotification(userId)) {
      return;
    }
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
      commonNotificationService.sendNotification("fcm", fcmToken.getToken(), userId,
          NotificationType.MATCH_ACCEPTED, "누군가 먼저 다가왔어요!", "당신을 기다리는 조각이 있어요. 마음이 닿으면 응답해보세요.");
    });
  }

  @Transactional
  public void sendMatchCompletedNotification(Long userId, String matchedUserName) {
    if (!isUserAllowingMatchNotification(userId)) {
      return;
    }
    fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
          String body = matchedUserName + "님과 연락처가 공개되었어요. 대화를 시작해보세요!";
          commonNotificationService.sendNotification("fcm", fcmToken.getToken(), userId,
              NotificationType.MATCH_COMPLETED, "퍼즐이 완성되었어요!", body);
        }
    );
  }
}
