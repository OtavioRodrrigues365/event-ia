package com.stefanini.event_ai.config;

import com.stefanini.event_ai.tools.EventAssistantTools;
import com.stefanini.event_ai.assistant.EventAssistantAiService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventAssistantConfig {

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    @Value("${gemini.model}")
    private String geminiModel;

    @Bean
    public GoogleAiGeminiChatModel googleAiGeminiChatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(geminiApiKey)
                .modelName(geminiModel)
                .temperature(0.7)
                .topP(0.9)
                .topK(40)
                .build();
    }

    @Bean
    public EventAssistantAiService assistant(GoogleAiGeminiChatModel model, EventAssistantTools assistantTools) {
        return AiServices.builder(EventAssistantAiService.class)
                .chatModel(model)
                .tools(assistantTools) // registra a ferramenta no Serviço IA
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}
