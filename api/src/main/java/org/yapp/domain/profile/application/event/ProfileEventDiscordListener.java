package org.yapp.domain.profile.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.profile.event.ProfileCreatedEvent;
import org.yapp.core.domain.profile.event.ProfileImageUpdatedEvent;
import org.yapp.core.domain.profile.event.ProfileRenewedEvent;
import org.yapp.core.domain.profile.event.ProfileValueTalkUpdatedEvent;
import org.yapp.infra.discord.DiscordMessageFactory;
import org.yapp.infra.discord.DiscordNotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileEventDiscordListener {

        private final DiscordNotificationService discordNotificationService;

        @EventListener
        public void handleProfileCreated(ProfileCreatedEvent event) {
                discordNotificationService.sendNotification(
                                DiscordMessageFactory.createNewProfileMessage(
                                                event.getProfileId(),
                                                event.getNickname()));
        }

        @EventListener
        public void handleProfileRenewed(ProfileRenewedEvent event) {
                discordNotificationService.sendNotification(
                                DiscordMessageFactory.createRenewProfileMessage(
                                                event.getProfileId(),
                                                event.getNickname()));
        }

        @EventListener
        public void handleProfileImageUpdated(ProfileImageUpdatedEvent event) {
                discordNotificationService.sendNotification(
                                DiscordMessageFactory.createProfileImageUpdateMessage(
                                                event.getProfileId(),
                                                event.getNickname()));
        }

        @EventListener
        public void handleProfileValueTalkUpdated(ProfileValueTalkUpdatedEvent event) {
                discordNotificationService.sendNotification(
                                DiscordMessageFactory.createProfileValueTalkUpdateMessage(
                                                event.getProfileId(),
                                                event.getNickname()));
        }
}