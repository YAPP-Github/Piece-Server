package org.yapp.domain.report.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.report.event.ReportEvent;
import org.yapp.infra.discord.DiscordMessageFactory;
import org.yapp.infra.discord.DiscordNotificationService;
import org.yapp.infra.discord.dto.DiscordMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportEventDiscordListener {

    private final DiscordNotificationService discordNotificationService;

    @Async
    @EventListener
    public void handleReportCreate(ReportEvent event) {
        DiscordMessage discordMessage = DiscordMessageFactory.createUserReportedMessage(
            event.getUserId(), event.getNickname(), event.getReason()
        );
        notifyWithLogging(event.getUserId(), "유저 신고 디스코드 알림 전송 실패", discordMessage);
    }

    private void notifyWithLogging(Long profileId, String errorMessage,
        DiscordMessage discordMessage) {
        try {
            discordNotificationService.sendNotification(discordMessage);
        } catch (Exception e) {
            log.error("{} 프로필 ID: {}, 에러: {}", errorMessage, profileId, e.getMessage(), e);
        }
    }
}