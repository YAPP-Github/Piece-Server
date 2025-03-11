package org.yapp.infra.discord.dto;

import java.util.List;

public record DiscordEmbed(
    String title,
    String description,
    Integer color,
    List<Field> fields
) {

    public static DiscordEmbed of(String title, String description, Integer color,
        List<Field> fields) {
        return new DiscordEmbed(title, description, color, fields);
    }

    public record Field(
        String name,
        String value,
        boolean inline
    ) {

        public static Field of(String name, String value, boolean inline) {
            return new Field(name, value, inline);
        }
    }
}