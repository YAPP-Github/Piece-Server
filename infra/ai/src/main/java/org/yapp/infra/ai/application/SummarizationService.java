package org.yapp.infra.ai.application;

import reactor.core.publisher.Mono;

public interface SummarizationService {

    Mono<String> summarize(String systemPrompt, String text);
}