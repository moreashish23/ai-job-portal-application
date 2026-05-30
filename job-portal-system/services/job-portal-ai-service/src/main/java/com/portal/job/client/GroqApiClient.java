package com.portal.job.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portal.job.exception.AiServiceException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroqApiClient {

    private final RestClient groqRestClient;

    @Value("${groq.api.model}")
    private String model;

    @Value("${groq.api.max-tokens}")
    private int maxTokens;

    @Value("${groq.api.temperature}")
    private double temperature;

    /**
     * Sends a prompt to Groq and returns the raw text response.
     * This is the single call site for all AI features.
     */
    public String complete(String systemPrompt, String userPrompt) {
        log.debug("Sending prompt to Groq — model={} maxTokens={}", model, maxTokens);

        ChatRequest request = ChatRequest.builder()
                .model(model)
                .maxTokens(maxTokens)
                .temperature(temperature)
                .messages(List.of(
                        new ChatMessage("system", systemPrompt),
                        new ChatMessage("user", userPrompt)
                ))
                .build();

        try {
            ChatResponse response = groqRestClient.post()
                    .uri("/chat/completions")
                    .body(request)
                    .retrieve()
                    .body(ChatResponse.class);

            if (response == null
                    || response.getChoices() == null
                    || response.getChoices().isEmpty()) {
                throw new AiServiceException("Groq API returned an empty response.");
            }

            String content = response.getChoices().get(0).getMessage().getContent();
            log.debug("Groq response received — length={}", content != null ? content.length() : 0);
            return content;

        } catch (RestClientException e) {
            log.error("Groq API call failed: {}", e.getMessage());
            throw new AiServiceException("AI service is temporarily unavailable. Please try again.", e);
        }
    }

    // ── Groq API request/response models ─────────────────────────────────────

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class ChatRequest {
        private String model;
        @JsonProperty("max_tokens")
        private int maxTokens;
        private double temperature;
        private List<ChatMessage> messages;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ChatMessage {
        private String role;
        private String content;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ChatResponse {
        private List<Choice> choices;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Choice {
        private ChatMessage message;
    }
}