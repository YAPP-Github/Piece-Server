package org.yapp.core.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.yapp.core.sse.dto.SsePayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Service
@Slf4j
public class FluxSsePersonalService implements SsePersonalService {

    private final Map<Long, Many<ServerSentEvent<Object>>> userSinks = new ConcurrentHashMap<>();

    public Flux<ServerSentEvent<Object>> connect(Long userId) {
        Sinks.Many<ServerSentEvent<Object>> sink = userSinks.compute(userId,
            (key, existingSink) -> {
                if (existingSink != null) {
                    existingSink.tryEmitComplete();
                }
                return Sinks.many().unicast().onBackpressureBuffer();
            });

        return sink.asFlux()
            .doOnCancel(() -> {
                log.info("User {} SSE connection canceled by client", userId);
                this.disconnect(userId);
            })
            .doFinally(signalType -> {
                log.info("User {} SSE connection closed. Signal: {}", userId, signalType);
                this.disconnect(userId);
            });
    }

    public void sendToPersonal(Long userId, SsePayload<Object> ssePayload) {
        Sinks.Many<ServerSentEvent<Object>> sink = userSinks.get(userId);
        if (sink != null) {
            sink.tryEmitNext(ServerSentEvent.builder()
                .id(ssePayload.getId())
                .event(ssePayload.getEventType().toString())
                .comment(ssePayload.getComment())
                .retry(ssePayload.getRetry())
                .data(ssePayload.getData())
                .build());
        }
    }

    public void disconnect(Long userId) {
        Sinks.Many<ServerSentEvent<Object>> sink = userSinks.remove(userId);
        if (sink != null) {
            sink.tryEmitComplete();
        }
    }
}
