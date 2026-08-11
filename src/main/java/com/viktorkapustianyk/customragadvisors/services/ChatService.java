package com.viktorkapustianyk.customragadvisors.services;

import com.viktorkapustianyk.customragadvisors.repo.ChatRepository;
import com.viktorkapustianyk.customragadvisors.model.Chat;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.viktorkapustianyk.customragadvisors.model.Role.ASSISTANT;
import static com.viktorkapustianyk.customragadvisors.model.Role.USER;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatClient chatClient;
    private final ChatEntryService chatEntryService;

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    public List<Chat> getAllChats() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Chat getChat(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
    }

    public Chat createChat(String title) {
        Chat chat = Chat.builder()
                .title(title)
                .build();
        chatRepository.save(chat);
        return chat;
    }

    public void deleteChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }

//    public void proceedInteraction(Long chatId, String prompt) {
//        chatEntryService.addChatEntry(chatId, prompt, USER);
//        String chatResponse = chatClient.prompt().user(prompt).call().content();
//        chatEntryService.addChatEntry(chatId, chatResponse, ASSISTANT);
//    }

    public void proceedInteraction(Long chatId, String prompt) {
        chatEntryService.addChatEntry(chatId, prompt, USER);

        log.info("Sending request to LLM: chatId={}, promptLength={}",
                chatId, prompt == null ? 0 : prompt.length());

        try {
            String chatResponse = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            log.info("Received response from LLM: chatId={}, responseLength={}",
                    chatId, chatResponse == null ? 0 : chatResponse.length());

            chatEntryService.addChatEntry(chatId, chatResponse, ASSISTANT);
        } catch (RuntimeException exception) {
            log.error("LLM request failed: chatId={}", chatId, exception);
            throw exception;
        }
    }
}
