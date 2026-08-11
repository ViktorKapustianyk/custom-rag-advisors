package com.viktorkapustianyk.customragadvisors.controller;

import com.viktorkapustianyk.customragadvisors.model.Chat;
import com.viktorkapustianyk.customragadvisors.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("chats", chatService.getAllChats());
        return "chat";
    }

    @GetMapping("/chat/{chatId}")
    public String showChat(Model model, @PathVariable Long chatId) {
        model.addAttribute("chats", chatService.getAllChats());
        model.addAttribute("chat", chatService.getChat(chatId));
        return "chat";
    }

    @PostMapping("/chat/new")
    public String newChat(@RequestParam String title){
        Chat chat = chatService.createChat(title);

        return "redirect:/chat/" + chat.getId();
    }

    @PostMapping("/chat/{chatId}/delete")
    public String deleteChat(@PathVariable Long chatId) {
        chatService.deleteChat(chatId);
        return "redirect:/";
    }

    @PostMapping("/chat/{chatId}/entry")
    public String talkToModel(@PathVariable Long chatId, @RequestParam String prompt) {
        chatService.proceedInteraction(chatId, prompt);
        return "redirect:/chat/" + chatId;
    }
}
