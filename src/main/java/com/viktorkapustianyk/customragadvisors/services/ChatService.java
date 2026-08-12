package com.viktorkapustianyk.customragadvisors.services;

import com.viktorkapustianyk.customragadvisors.repo.ChatRepository;
import com.viktorkapustianyk.customragadvisors.model.Chat;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static com.viktorkapustianyk.customragadvisors.model.Role.ASSISTANT;
import static com.viktorkapustianyk.customragadvisors.model.Role.USER;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatClient chatClient;
    private final ChatEntryService chatEntryService;

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

    public void proceedInteraction(Long chatId, String prompt) {
        chatEntryService.addChatEntry(chatId, prompt, USER);
        String chatResponse = chatClient.prompt().user(prompt).call().content();
        chatEntryService.addChatEntry(chatId, chatResponse, ASSISTANT);
    }

    public SseEmitter proceedInteractionWithStreaming(Long chatId, String prompt) {
        chatEntryService.addChatEntry(chatId, prompt, USER);

        StringBuilder answer = new StringBuilder();

        SseEmitter sseEmitter = new SseEmitter(0L);

        chatClient.prompt().user(prompt).stream()
                .chatResponse()
                .subscribe(chatResponse -> processToken(chatResponse, sseEmitter, answer),
                        sseEmitter::completeWithError,
                        ()-> chatEntryService.addChatEntry(chatId, answer.toString(), ASSISTANT));

        return sseEmitter;
    }

    @SneakyThrows
    private void processToken(ChatResponse chatResponse, SseEmitter sseEmitter, StringBuilder answer) {
        AssistantMessage token = chatResponse.getResult().getOutput();
        sseEmitter.send(token);
        answer.append(token.getText());
    }
}
