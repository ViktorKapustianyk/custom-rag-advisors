package com.viktorkapustianyk.customragadvisors.config;

import com.viktorkapustianyk.customragadvisors.repo.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final ChatRepository chatRepository;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultAdvisors(getChatMemoryAdvisor()).build();
    }

    private Advisor getChatMemoryAdvisor() {
        return MessageChatMemoryAdvisor.builder(getChatMemory()).build();
    }

    private ChatMemory getChatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(2)
                .chatMemoryRepository(chatRepository)
                .build();
    }
}
