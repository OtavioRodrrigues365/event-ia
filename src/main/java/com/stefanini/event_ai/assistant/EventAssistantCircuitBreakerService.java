package com.stefanini.event_ai.assistant;

import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventAssistantCircuitBreakerService {

    private final EventAssistantAiService eventAssistantAiService;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public EventAssistantCircuitBreakerService(EventAssistantAiService eventAssistantAiService,
                                              CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.eventAssistantAiService = eventAssistantAiService;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public String askAssistant(String userMessage) {
        var circuitBreaker = circuitBreakerFactory.create("geminiAssistant");

        return circuitBreaker.run(() -> {
            Result<String> result = eventAssistantAiService.handleRequest(userMessage);

            TokenUsage tokenUsage = result.tokenUsage();
            if (tokenUsage != null) {
                int inputTokens = tokenUsage.inputTokenCount();
                int outputTokens = tokenUsage.outputTokenCount();
                System.out.println("Custo mapeado -> Input: " + inputTokens + " | Output: " + outputTokens);
            }

            return result.content();
        }, throwable -> fallbackResponse());
    }

    private String fallbackResponse() {
        return """
               O assistente está temporariamente indisponível. Tente novamente em alguns instantes.
                """;
    }
}
