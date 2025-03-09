package org.yapp.infra.discord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.yapp.infra.discord.dto.DiscordMessage;
import org.yapp.infra.discord.dto.DiscordMessageType;

@Service
@Slf4j
public class DiscordNotificationService {

    private final String DISCORD_ERROR_WEBHOOK_URL;
    private final String DISCORD_SERVICE_WEBHOOK_URL;
    private final WebClient webClient;

    public DiscordNotificationService(
        @Value("${discord.logging.error.webhook-url}") String errorWebHookUrl,
        @Value("${discord.logging.service.webhook-url}") String serviceWebhookUrl) {
        this.DISCORD_ERROR_WEBHOOK_URL = errorWebHookUrl;
        this.DISCORD_SERVICE_WEBHOOK_URL = serviceWebhookUrl;
        this.webClient = WebClient.builder().build();
    }

    public void sendNotification(DiscordMessage message) {
        String webhookUrl = this.getWebhookUrl(message.messageType());

        webClient.post()
            .uri(webhookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(message)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(
                e -> log.info("Failed to send Discord notification: " + e.getMessage()))
            .subscribe();
    }

    private String getWebhookUrl(DiscordMessageType discordMessageType) {
        if (DiscordMessageType.ERROR_MESSAGE.equals(discordMessageType)) {
            return DISCORD_ERROR_WEBHOOK_URL;
        } else if (DiscordMessageType.SERVICE_MESSAGE.equals(discordMessageType)) {
            return DISCORD_SERVICE_WEBHOOK_URL;
        } else {
            throw new IllegalArgumentException(
                "지원하지 않는 디스코드 메시지 타입: " + discordMessageType);
        }
    }
}