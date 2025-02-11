package org.yapp.core.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.yapp.core.sse.dto.SsePayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Service
@Slf4j
public class FluxSsePersonalService implements SsePersonalService {

    private final Map<Long, FluxSink<ServerSentEvent<Object>>> userSinks = new ConcurrentHashMap<>();

    public Flux<ServerSentEvent<Object>> connect(Long userId) {
        return Flux.create(sink -> {
            userSinks.put(userId, sink);

            sink.onDispose(() -> {
                userSinks.remove(userId);
                log.info("#onDispose 사용자 {} SSE 연결이 종료되었습니다.", userId);
            });

            sink.onCancel(() -> {
                this.disconnect(userId);
                log.info("#onCancel 사용자 {} SSE 연결이 종료되었습니다.", userId);
            });

        }, FluxSink.OverflowStrategy.BUFFER);
    }

    public void sendToPersonal(Long userId, SsePayload<Object> ssePayload) {
        FluxSink<ServerSentEvent<Object>> sink = userSinks.get(userId);
        if (sink != null) {
            sink.next(ServerSentEvent.builder()
                .id(ssePayload.getId())
                .event(ssePayload.getEventType().toString())
                .data(ssePayload.getData())
                .build());
        }
    }

    public void disconnect(Long userId) {
        FluxSink<ServerSentEvent<Object>> sink = userSinks.remove(userId);
        if (sink != null) {
            try {
                sink.complete();
            } catch (Exception e) {
                log.warn("사용자 {} SSE 연결 종료 실패: {}", userId, e.getMessage());
            }
        } else {
            log.warn("사용자 {} SSE 연결을 찾을 수 없습니다.", userId);
        }
    }
}
