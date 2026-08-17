package com.viktorkapustianyk.customragadvisors.repo;

import com.viktorkapustianyk.customragadvisors.model.Chat;
import com.viktorkapustianyk.customragadvisors.model.ChatEntry;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatMemoryRepository {
    @Override
    default List<String> findConversationIds(){
        return findAll().stream().map(Chat::getId).map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    default List<Message> findByConversationId(String conversationId){
        Chat chat = findById(Long.valueOf(conversationId)).orElseThrow();
        return chat.getHistory().stream().map(ChatEntry::toMessage).collect(Collectors.toList());
    }

    @Override
    default void saveAll(String conversationId, List<Message> messages){
        Chat chat = findById(Long.valueOf(conversationId)).orElseThrow();
        chat.getHistory().clear();
        chat.getHistory().addAll(messages.stream().map(ChatEntry::toChatEntry).toList());
        save(chat);
    }

    @Override
    default void deleteByConversationId(String conversationId){
        // not implemented
    }
}
