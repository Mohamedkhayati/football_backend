package com.football.assistant.config;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.model.chat.ChatLanguageModel;
import com.football.assistant.service.FootballAgentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FootballAgentConfig {

    @Bean
    public FootballAgentService footballAgentService(ChatLanguageModel chatLanguageModel) {
        return AiServices.create(FootballAgentService.class, chatLanguageModel);
    }
}
