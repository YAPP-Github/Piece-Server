package org.yapp.sse.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.core.sse.FluxSsePersonalService;
import org.yapp.format.CommonResponse;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {

    private final FluxSsePersonalService ssePersonalService;

    @PreAuthorize(value = "hasAuthority('USER')")
    @GetMapping("/personal/connect")
    @Operation(summary = "SSE 연결", description = "로그인 회원에 대한 이벤트 수신 스트림을 얻습니다", tags = {"SSE"})
    public Flux<ServerSentEvent<Object>> connect(@AuthenticationPrincipal Long userId) {
        return ssePersonalService.connect(userId);
    }

    @PreAuthorize(value = "hasAuthority('USER')")
    @DeleteMapping("/personal/disconnect")
    @Operation(summary = "SSE 연결 해제", description = "로그인 회원의 SSE 연결을 해제합니다", tags = {"SSE"})
    public ResponseEntity<CommonResponse<Void>> disconnect(@AuthenticationPrincipal Long userId) {
        ssePersonalService.disconnect(userId);
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent("SSE 연결이 해제 되었습니다"));
    }
}
