package org.yapp.core.sse.dto;

import java.time.Duration;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Getter
@Builder
public class SsePayload<T> {

    private static final EventType DEFAULT_EVENT_TYPE = EventType.NOTIFICATION;
    private static final Duration DEFAULT_DURATION = Duration.ofMinutes(10);
    private static final String DEFAULT_COMMENT = "SSE Payload";
    private static final Duration DEFAULT_RETRY = Duration.ofSeconds(5);

    private String id;
    private EventType eventType;
    private Duration duration;
    private String comment;
    private Duration retry;
    private T data;

    public static <T> SsePayload<T> of(String id, EventType eventType, T data) {
        return SsePayload.<T>builder()
            .id(id != null ? id : UUID.randomUUID().toString())
            .eventType(eventType != null ? eventType : DEFAULT_EVENT_TYPE)
            .duration(DEFAULT_DURATION)
            .comment(DEFAULT_COMMENT)
            .retry(DEFAULT_RETRY)
            .data(data)
            .build();
    }
}