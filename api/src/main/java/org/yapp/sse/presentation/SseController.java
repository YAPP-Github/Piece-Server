package org.yapp.sse.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.core.sse.FluxSsePersonalService;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {

    private final FluxSsePersonalService ssePersonalService;

    @PreAuthorize(value = "hasRole('USER')")
    @PostMapping("/personal/connect")
    @Operation(summary = "SSE 연결", description = "로그인 회원에 대한 이벤트 수신 스트림을 얻습니다", tags = {"SSE"})
    public Flux<ServerSentEvent<Object>> connect(@AuthenticationPrincipal Long userId) {
        return ssePersonalService.connect(userId);
    }
}
