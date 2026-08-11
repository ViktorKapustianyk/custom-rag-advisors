package com.viktorkapustianyk.customragadvisors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomRagAdvisorsApplication {

//    @Bean
//    public ChatClient chatClient(ChatClient.Builder builder) {
//        return builder.build();
//    }

    public static void main(String[] args) {
        SpringApplication.run(CustomRagAdvisorsApplication.class, args);

//        ChatClient chatClient = SpringApplication.run(CustomRagAdvisorsApplication.class, args).getBean(ChatClient.class);
//        System.out.println(chatClient.prompt()
//                .user("Give me the text of the song Bohemian Rhapsody")
//                .call()
//                .content());
    }

}
