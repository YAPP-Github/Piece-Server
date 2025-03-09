package org.yapp.infra.discord.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public record DiscordMessage(
    @JsonIgnore
    DiscordMessageType messageType,
    String content,
    List<DiscordEmbed> embeds
) {

    public static DiscordMessage of(DiscordMessageType messageType, String content,
        List<DiscordEmbed> embeds) {
        return new DiscordMessage(messageType, content, embeds);
    }
}