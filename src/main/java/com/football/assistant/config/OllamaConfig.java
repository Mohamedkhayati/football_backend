package com.football.assistant.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OllamaConfig {

    private static final String BASE_URL = "http://localhost:11434";
    private static final String CHAT_MODEL = "qwen3:1.7b";   // or , phi3:mini ...
    private static final int TIMEOUT_SECONDS = 300;
    private static final String IMAGE_MODEL = "llava";

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(CHAT_MODEL)
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .temperature(0.7)
                .build();
    }

    @Bean
    public ChatLanguageModel imageChatModel() {
        return OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(IMAGE_MODEL)
                .timeout(Duration.ofSeconds(180))
                .temperature(0.7)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }
}
