package com.viktorkapustianyk.customragadvisors.services;

import com.viktorkapustianyk.customragadvisors.model.Chat;
import com.viktorkapustianyk.customragadvisors.model.ChatEntry;
import com.viktorkapustianyk.customragadvisors.model.Role;
import com.viktorkapustianyk.customragadvisors.repo.ChatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatEntryService {
    @Autowired
    private ChatRepository chatRepository;

    @Transactional
    public void addChatEntry(Long chatId, String prompt, Role role) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        chat.addEntry(ChatEntry.builder()
                .content(prompt)
                .role(role)
                .build());
    }
}
