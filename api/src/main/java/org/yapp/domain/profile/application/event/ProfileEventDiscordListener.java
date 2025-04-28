package org.yapp.domain.profile.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.profile.event.ProfileCreatedEvent;
import org.yapp.core.domain.profile.event.ProfileImageUpdatedEvent;
import org.yapp.core.domain.profile.event.ProfileRenewedEvent;
import org.yapp.core.domain.profile.event.ProfileValueTalkUpdatedEvent;
import org.yapp.infra.discord.DiscordMessageFactory;
import org.yapp.infra.discord.DiscordNotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileEventDiscordListener {

    private final DiscordNotificationService discordNotificationService;

    @Async
    @EventListener
    public void handleProfileCreated(ProfileCreatedEvent event) {
        try {
            discordNotificationService.sendNotification(
                DiscordMessageFactory.createNewProfileMessage(
                    event.getProfileId(),
                    event.getNickname()));
        } catch (Exception e) {
            log.error("프로필 생성 디스코드 알림 전송 실패. 프로필 ID: {}, 에러: {}",
                event.getProfileId(), e.getMessage(), e);
        }
    }

    @Async
    @EventListener
    public void handleProfileRenewed(ProfileRenewedEvent event) {
        try {
            discordNotificationService.sendNotification(
                DiscordMessageFactory.createRenewProfileMessage(
                    event.getProfileId(),
                    event.getNickname()));
        } catch (Exception e) {
            log.error("프로필 갱신 디스코드 알림 전송 실패. 프로필 ID: {}, 에러: {}",
                event.getProfileId(), e.getMessage(), e);
        }
    }

    @Async
    @EventListener
    public void handleProfileImageUpdated(ProfileImageUpdatedEvent event) {
        try {
            discordNotificationService.sendNotification(
                DiscordMessageFactory.createProfileImageUpdateMessage(
                    event.getProfileId(),
                    event.getNickname()));
        } catch (Exception e) {
            log.error("프로필 이미지 업데이트 디스코드 알림 전송 실패. 프로필 ID: {}, 에러: {}",
                event.getProfileId(), e.getMessage(), e);
        }
    }

    @Async
    @EventListener
    public void handleProfileValueTalkUpdated(ProfileValueTalkUpdatedEvent event) {
        try {
            discordNotificationService.sendNotification(
                DiscordMessageFactory.createProfileValueTalkUpdateMessage(

                    event.getProfileId(),
                    event.getNickname()));
        } catch (Exception e) {
            log.error("프로필 가치관 톡 업데이트 디스코드 알림 전송 실패. 프로필 ID: {}, 에러: {}",
                event.getProfileId(), e.getMessage(), e);
        }
    }
}