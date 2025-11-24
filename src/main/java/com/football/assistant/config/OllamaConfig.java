package com.football.assistant.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OllamaConfig {
    
    @Value("${ollama.base-url}")
    private String baseUrl;
    
    @Value("${ollama.model-name}")
    private String modelName;
    
    @Value("${ollama.timeout}")
    private int timeout;
    
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(timeout))
                .temperature(0.7)
                .build();
    }
    @Bean
    public ChatLanguageModel imageChatModel(
        @Value("${ollama.base-url}") String baseUrl,
        @Value("${ollama.image-model}") String imageModelName) {
        return OllamaChatModel.builder()
            .baseUrl(baseUrl)
            .modelName(imageModelName)
            .timeout(Duration.ofSeconds(180))
            .temperature(0.7)
            .build();
    }
    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }
}
