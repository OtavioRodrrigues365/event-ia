package com.stefanini.event_ai.controller;

import com.stefanini.event_ai.assistant.EventAssistantCircuitBreakerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event/assistant")
public class EventAssistantController {

    private final EventAssistantCircuitBreakerService eventAssistantCircuitBreakerService;

    public EventAssistantController(EventAssistantCircuitBreakerService eventAssistantCircuitBreakerService) {
        this.eventAssistantCircuitBreakerService = eventAssistantCircuitBreakerService;
    }


    @PostMapping()
    public String askAssistant(@RequestBody String userMessage) {
        return eventAssistantCircuitBreakerService.askAssistant(userMessage);
    }
}
