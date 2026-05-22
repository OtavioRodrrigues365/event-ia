package com.stefanini.event_ai.controller;

import com.stefanini.event_ai.assistant.EventAssistantAiService;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event/assistant")
public class EventAssistantController {

    private final EventAssistantAiService eventAssistantAiService;

    public EventAssistantController(EventAssistantAiService eventAssistantAiService) {
        this.eventAssistantAiService = eventAssistantAiService;
    }


    @PostMapping()
    @CircuitBreaker(name = "geminiAssistant", fallbackMethod = "fallbackResponse")
    public String askAssistant(@RequestBody String userMessage) {
        Result<String> result = eventAssistantAiService.handleRequest(userMessage);

        TokenUsage tokenUsage = result.tokenUsage();
        if (tokenUsage != null) {
            int inputTokens = tokenUsage.inputTokenCount();
            int outputTokens = tokenUsage.outputTokenCount();
            System.out.println("Custo mapeado -> Input: " + inputTokens + " | Output: " + outputTokens);
        }

        return result.content();
    }


    private String fallbackResponse() {
        return """
               O assistente está temporariamente indisponível. Tente novamente em alguns instantes.
                """;
    }
}
