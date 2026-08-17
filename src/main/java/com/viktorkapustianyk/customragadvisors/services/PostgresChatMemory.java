package com.viktorkapustianyk.customragadvisors.services;

import com.viktorkapustianyk.customragadvisors.model.Chat;
import com.viktorkapustianyk.customragadvisors.model.ChatEntry;
import com.viktorkapustianyk.customragadvisors.model.Role;
import com.viktorkapustianyk.customragadvisors.repo.ChatRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostgresChatMemory implements ChatMemory {
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void add(String conversationId, List<Message> messages) {
        for (Message message : messages){
            Chat chat = chatRepository.findById(Long.valueOf(conversationId)).orElseThrow();
            chat.addEntry(ChatEntry.toChatEntry(message));
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        Chat chat = chatRepository.findById(Long.valueOf(conversationId)).orElseThrow();

        return chat.getHistory().stream()
                .map(entry -> entry.getRole() == Role.USER
                        ? new UserMessage(entry.getContent())
                        : new AssistantMessage(entry.getContent()))
                .collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        //not implemented
    }
}
