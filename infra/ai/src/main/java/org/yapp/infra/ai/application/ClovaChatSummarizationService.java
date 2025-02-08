package org.yapp.infra.ai.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ClovaChatSummarizationService implements SummarizationService {

    private final WebClient webClient;

    @Value("${ncp.clova-studio.endpoint}")
    private String apiUrl;

    @Value("${ncp.clova-studio.api-key}")
    private String apiKey;

    @Value("${ncp.clova-studio.request-id}")
    private String requestId;

    private final String AUTHORIZATION_PREFIX = "Bearer ";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ClovaChatSummarizationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl(apiUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE)
            .build();
    }

    public Mono<String> summarize(String systemPrompt, String text) {
        return webClient.post()
            .uri(apiUrl)
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
                "maxTokens", 10,
                "temperature", 0.8,
                "repeatPenalty", 8.0,
                "stopBefore", List.of(),
                "includeAiFilters", true,
                "seed", 0
            ))
            .retrieve()
            .bodyToFlux(String.class)
            .collectList()
            .map(this::extractFinalResponse);
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

            }
        }

        return lastContent;
    }
}