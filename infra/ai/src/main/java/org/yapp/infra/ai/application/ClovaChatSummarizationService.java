package org.yapp.infra.ai.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@Slf4j
public class ClovaChatSummarizationService implements SummarizationService {

    private final WebClient webClient;

    private final String apiKey;

    private final String requestId;

    private final String AUTHORIZATION_PREFIX = "Bearer ";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ClovaChatSummarizationService(
        WebClient.Builder webClientBuilder,
        @Value("${ncp.clova-studio.endpoint}") String apiUrl,
        @Value("${ncp.clova-studio.api-key}") String apiKey,
        @Value("${ncp.clova-studio.request-id}") String requestId
    ) {
        this.apiKey = apiKey;
        this.requestId = requestId;
        this.webClient = webClientBuilder
            .baseUrl(apiUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE)
            .build();
    }

    public Mono<String> summarize(String systemPrompt, String text) {
        return webClient.post()
            .header("Authorization", AUTHORIZATION_PREFIX + apiKey)
            .header("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId)
            .header("Content-Type", "application/json")
            .header("Accept", "text/event-stream")
            .bodyValue(Map.of(
                "messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", text)
                ),
                "topP", 0.8,
                "topK", 0,
                "maxTokens", 30,
                "temperature", 0.8,
                "repeatPenalty", 8.0,
                "stopBefore", List.of(),
                "includeAiFilters", true,
                "seed", 0
            ))
            .retrieve()
            .bodyToFlux(String.class)
            .collectList()
            .map(this::extractFinalResponse)
            .retryWhen(
                Retry.backoff(2, Duration.ofMillis(1000))
                    .maxBackoff(Duration.ofSeconds(2))
                    .filter(throwable -> {
                        log.warn("요약 API 재시도 대상 오류: {}", throwable.toString());
                        return true;
                    })
            ).onErrorResume(e -> {
                log.error("요약 API 최종 실패: {}", e.toString());
                return Mono.just("요약을 진행할 수 없습니다.");
            });
    }

    private String extractFinalResponse(List<String> jsonChunks) {
        String lastContent = "";

        for (String jsonChunk : jsonChunks) {
            try {
                JsonNode node = objectMapper.readTree(jsonChunk);
                if (node.has("message") && node.get("message").has("content")) {
                    lastContent = node.get("message").get("content")
                        .asText();
                }
            } catch (Exception ignored) {
                log.warn("JSON 파싱 중 오류 발생: {}, 데이터: {}", ignored.getMessage(), jsonChunk);
                return "요약에 실패했어요.";
            }
        }

        return lastContent;
    }
}