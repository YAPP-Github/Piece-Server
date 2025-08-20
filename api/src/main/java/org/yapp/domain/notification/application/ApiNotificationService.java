package org.yapp.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.notification.enums.NotificationType;
import org.yapp.core.domain.setting.Setting;
import org.yapp.core.notification.application.NotificationService;
import org.yapp.core.notification.dao.FcmTokenRepository;
import org.yapp.domain.setting.application.SettingService;

@RequiredArgsConstructor
@Service
public class ApiNotificationService {

    private final NotificationService notificationService;
    private final FcmTokenRepository fcmTokenRepository;
    private final SettingService settingService;
    private final NotificationQueueService notificationQueueService;

    @Transactional
    public void reserveNewMatchNotification(Long userId) {
        if (!isUserAllowingMatchNotification(userId)) {
            return;
        }
        notificationQueueService.pushUserIdToMatchNotificationQueue(userId);
    }

    private boolean isUserAllowingMatchNotification(Long userId) {
        Setting userSetting = settingService.getUserSetting(userId);
        return userSetting.isNotification() && userSetting.isMatchNotification();
    }

    @Async
    @Transactional
    public void sendNewMatchNotification(Long userId) {
        fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
            notificationService.sendNotification("fcm", fcmToken.getToken(), userId,
                NotificationType.MATCH_NEW, "새로운 인연 도착!", "지금 인연을 확인해보세요.");
        });
    }

    @Async
    @Transactional
    public void sendMatchAcceptedNotification(Long userId) {
        if (!isUserAllowingMatchNotification(userId)) {
            return;
        }
        fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
            notificationService.sendNotification("fcm", fcmToken.getToken(), userId,
                NotificationType.MATCH_ACCEPTED, "누군가 먼저 다가왔어요!",
                "내 운명일지도 몰라요! 지금 확인해보세요.");
        });
    }

    @Async
    @Transactional
    public void sendMatchCompletedNotification(Long userId, String matchedUserName) {
        if (!isUserAllowingMatchNotification(userId)) {
            return;
        }
        fcmTokenRepository.findByUserId(userId).ifPresent(fcmToken -> {
                String title = matchedUserName + "님과 마음이 통했어요!";
                String body = "연락처가 공개되었어요. 대화를 시작해보세요!";
                notificationService.sendNotification("fcm", fcmToken.getToken(), userId,
                    NotificationType.MATCH_COMPLETED, title, body);
            }
        );
    }
}
